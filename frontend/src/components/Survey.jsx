import React from 'react'
import { useState } from "react";
import Bar from './Bar';
import { useEffect } from 'react';

const Survey = (props) => {

    const [show, setShow] = useState(true)
    const [survey, setSurvey] = useState([])

    useEffect(() => {
        setSurvey(props.SurveyData);
    }, [props.SurveyData])

    return (
        <>
            <Bar name="Survey" setShow={setShow} />

            {
                show ? <div className="survey">
                    <div>Survey</div>
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