import React from 'react'

const Bar = (props) => {

    const handleOnClick = () => {
        props.setShow(prev => !prev)
    }


    return (
        <div className='bar' onClick={handleOnClick}>
            {props.name}
        </div>
    )
}

export default Bar