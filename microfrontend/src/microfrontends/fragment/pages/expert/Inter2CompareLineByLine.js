import React from 'react'
import info from '../../../../resources/assets/information.svg'
import ReactTooltip from 'react-tooltip';

const Inter2CompareLineByLine = (props) => {
    return (
        <div className="">
            <div style={{textAlign:"left", display:"flex"}}>
                <p style={{fontSize:"18px", color:"#333", fontWeight:500}}>{props.data.inters[0].title}</p>
                <img alt="info" src={info} data-tip={props.messages.info_highlighting}
                            className="fragment-info"></img>
                <ReactTooltip backgroundColor="#fff" textColor="#333" border={true} borderColor="#000" className="reading-tooltip" place="bottom" effect="solid"/>

            </div>
            <div className="well" style={{fontSize:"14px", lineHeight:"1.42857", fontFamily:props.spaces?"monospace":"georgia"}} dangerouslySetInnerHTML={{ __html: props.data.writerLineByLine }}></div>
        </div>
    )
}

export default Inter2CompareLineByLine