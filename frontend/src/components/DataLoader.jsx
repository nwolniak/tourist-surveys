import React, {useEffect, useState} from "react";
import Bar from "./Bar";

// import uploadImg from "../public/upload.png/";

function DataLoader(props) {
    const [show, setShow] = useState(true);
    const [file, setFile] = useState();
    const [array, setArray] = useState([]);

    const handleOnChange = (e) => {
        setFile(e.target.files[0]);

        if (e.target.files[0]) {
            const fileReader = new FileReader();
            fileReader.onload = function (event) {
                const text = event.target.result;
                var fileName = e.target.files[0].name;
                if (fileName.endsWith(".csv")) {
                    csvFileToArray(text);
                    sendFileToServer(text, "csv")
                } else if (fileName.endsWith(".json")) {
                    jsonFileToArray(text);
                    sendFileToServer(text, "json")
                }
            };
            fileReader.readAsText(e.target.files[0]);
        }
    };

    const sendFileToServer = async (file, type) => {
        const requestData = {
            file: file,
            type: type
        };
        try {
            const response = await fetch("http://localhost:8080/surveyResults", {
                method: "POST",
                mode: "cors",
                credentials: "same-origin",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(requestData),
            });
            const jsonData = await response.json();
            props.setSurveyData(jsonData);
        } catch (err) {
            console.error(err.message);
        }
    }

    const csvFileToArray = (string) => {
        const csvHeader = string.slice(0, string.indexOf("\n")).split(";");
        const csvRows = string.slice(string.indexOf("\n") + 1).split("\n");
        const array = csvRows.map((i) => {
            const values = i.split(";");
            const obj = csvHeader.reduce((object, header, index) => {
                object[header] = values[index];
                return object;
            }, {});
            return obj;
        });
        setArray(array);
    };

    const jsonFileToArray = (string) => {
        var poisWithTimestamps = JSON.parse(string)
            .trajectory
            .poisWithTimestamps;
        var poisMapped = poisWithTimestamps.map(element => ({
            year: element["first"].date.year,
            month: element["first"].date.month,
            day: element["first"].date.day,
            hour: element["first"].time.hour,
            minute: element["first"].time.minute,
            second: element["first"].time.second,
            nano: element["first"].time.nano,
            latitude: element["second"].lat != null ? element["second"].lat : element["second"].osmModel.lat,
            longitude: element["second"].lon != null ? element["second"].lon : element["second"].osmModel.lon
        }));
        var array = poisMapped.map(element => ({
            latitude: element.latitude.toString(),
            longitude: element.longitude.toString(),
            timestamp: new Date(
                element.year,
                element.month - 1,
                element.day,
                element.hour,
                element.minute,
                element.second,
                element.nano / 1000000
            ).toISOString()
        }))
        setArray(array);
    }


    const prepareMarkerArray = () => {
        const markers = array
            .sort((item1, item2) => new Date(item1.timestamp) - new Date(item2.timestamp))
            .map(item => {
                const lattitude = item["latitude"]
                const longitude = item["longitude"]

                const newLattitude = lattitude ? parseFloat(lattitude.replace(",", ".")) : ""
                const newLongitude = longitude ? parseFloat(longitude.replace(",", ".")) : ""

                if (item["timestamp"] !== undefined) {
                    return {
                        geocode: [newLattitude, newLongitude],
                        timestamp: item["timestamp"]
                    }
                } else {
                    return null
                }
            }).filter(item => item !== null)
        console.log(markers)
        props.handleMapData(markers)
    }

    useEffect(() => {
        prepareMarkerArray()
    }, [array])


    return (
        <>
            <Bar name="Data loader" setShow={setShow}/>
            {
                show ?
                    <div className="dataLoader">
                        <div className="app">
                            <div className="parent">
                                <div className="file-upload">
                                    <img src="./upload.png" alt="upload"/>
                                    <h3>Click box to upload</h3>
                                    <input type="file" onChange={handleOnChange}/>
                                </div>
                            </div>
                        </div>
                    </div> : null
            }
        </>
    );
}

export default DataLoader;
