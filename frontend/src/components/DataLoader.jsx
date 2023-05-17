import { marker } from "leaflet";
import React, { useEffect, useState } from "react";
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
        csvFileToArray(text);
        sendCSVtoServer(text)
      };
      
      fileReader.readAsText(e.target.files[0]);
    }
  };

  const sendCSVtoServer = async (csv) => {

    const requestData = {
      file: csv,
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

  const prepareMarkerArray = () => {
    const markers = array.map(item => {
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
      <Bar name="Data loader" setShow={setShow} />
      {
        show ?
          <div className="dataLoader">
            <div className="app">
              <div className="parent">
                <div className="file-upload">
                  <img src="./upload.png" alt="upload" />
                  <h3>Click box to upload</h3>
                  <input type="file" onChange={handleOnChange} />
                </div>
              </div>
            </div>
          </div> : null
      }
    </>
  );
}

export default DataLoader;
