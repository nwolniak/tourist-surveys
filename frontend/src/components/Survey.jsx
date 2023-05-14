import React from 'react'
import { useState } from "react";
import Bar from './Bar';

const Survey = () => {

    const [show, setShow] = useState(true)
    const [survey, setSurvey] = useState([])

    const handleOnClick = async () => {
        try {
            const response = await fetch("http://localhost:8080/getSurveyResults", {
                method: "GET",
                mode: "cors",
                credentials: "same-origin",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            const jsonData = await response.json();
            console.log(jsonData);
            setSurvey(jsonData);
        } catch (err) {
            console.error(err.message);
        }

    }

    return (
        <>
            <Bar name="Survey" setShow={setShow} />

            {
                show ? <div className="survey">
                    <div>Survey</div>
                    {/* temporary solution */}
                    <button onClick={() => handleOnClick()}>Get data</button>
                    <table>
                        <thead>
                            <tr>
                                <th>Question</th>
                                <th>Answer</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                survey.map((item, index) => {
                                    return (
                                        <tr key={index}>
                                            <td>{item.question}</td>
                                            <td>{item.answer}</td>
                                        </tr>
                                    )
                                }
                                )
                            }
                        </tbody>
                    </table>
                </div> : null
            }
        </>

    )
}

export default Survey