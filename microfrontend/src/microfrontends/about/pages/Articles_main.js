import React from 'react'
import ArticlesEn from './Articles_en'
import ArticlesEs from './Articles_es'
import ArticlesPt from './Articles_pt'



const Articles_main = (props) => {
    return(
        <div>
            <div className="about-container">
                <p className="about-conduct-title">{props.messages.header_bibliography}</p>
                {props.language==="pt"?<ArticlesPt/>:props.language==="en"?<ArticlesEn/>:<ArticlesEs/>}
            </div>
        </div>
        
    )
}

export default Articles_main