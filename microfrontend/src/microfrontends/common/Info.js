import React from 'react'
import logotiposm from '../../resources/assets/logotiposm.png'
import logotipos from '../../resources/assets/logotipos.png'
import '../../resources/css/about/About.css'
import {useLocation} from 'react-router-dom'

const Info = (props) => {
    const location = useLocation();
    return(
        <div >
            <div className={location.pathname==="/"?"home-info":"info"}>
                <img alt="img" className="info-img" src={logotipos}></img>
                <img alt="img" className="info-img-mobile" src={logotiposm}></img>
                {props.language==="pt"?
                <p className="info-end">O <em>Arquivo LdoD</em> foi desenvolvido no &acirc;mbito do projeto de investiga&ccedil;&atilde;o &ldquo;
                    Nenhum Problema Tem Solu&ccedil;&atilde;o: Um Arquivo Digital do <em>Livro do Desassossego</em>&rdquo; 
                    (PTDC/CLE-LLI/118713/2010) do Centro de Literatura Portuguesa da Universidade de Coimbra (CLP). 
                    Projeto financiado pela Funda&ccedil;&atilde;o para a Ci&ecirc;ncia e a Tecnologia (FCT) e 	cofinanciado pelo Fundo Europeu de 
                    Desenvolvimento Regional (FEDER), atrav&eacute;s do Eixo I do Programa Operacional Fatores de Competitividade (POFC) do Quadro 
                    de Refer&ecirc;ncia Estrat&eacute;gica Nacional (QREN)&mdash;Uni&atilde;o Europeia (COMPETE: FCOMP-01-0124-FEDER-019715). 
                    Financiado ainda por Fundos Nacionais atrav&eacute;s da Funda&ccedil;&atilde;o para a Ci&ecirc;ncia e a Tecnologia no &acirc;mbito 
                    dos projetos de &ldquo;Financiamento Plurianual &mdash;Unidade 759&rdquo;: &ldquo;PEst-OE/ELT/UI00759/2013&rdquo; e &ldquo;PEst-OE/ELT/UI0759/2014&rdquo;. 
                </p>
                :props.language==="en"?
                <p className="info-end">The <em>LdoD Archive</em> was developed under the research project &ldquo;No Problem
                    Has a Solution: A Digital Archive of the <em>Book of Disquiet</em>,&rdquo; 
                    (PTDC/CLE-LLI/118713/2010) of the Centre for Portuguese Literature at the University of 
                    Coimbra (CLP). Project funded by the Foundation for Science and Technology (FCT) and 
                    co-funded by the European Regional Development Fund (FEDER), through Axis I of the 
                    Competitiveness Factors Operational Program (POFC) of the National Strategic Framework 
                    (QREN)&mdash;European Union (COMPETE: FCOMP-01-0124-FEDER-019715). Additional national 
                    funds by the Foundation for Science and Technology under the &ldquo;Multi-annual 
                    Financing&mdash;Unit 759&rdquo; projects: &ldquo;PEst-OE/ELT/UI00759/2013&rdquo; and
                    &ldquo;PEst-OE/ELT/UI0759/2014&rdquo;.
                </p>
                :<p className="info-end">
                    El <em>Archivo LdoD</em> resulta del
                    proyecto de investigaci&oacute;n &ldquo;Ning&uacute;n problema tiene
                    soluci&oacute;n: Un archivo digital del <em>Libro del desasosiego</em>&rdquo;,
                    (PTDC/CLE-LLI/118713/2010) del Centro de Literatura Portuguesa de la Universidad de Coimbra. 
                    Proyecto financiado por la Fundaci&oacute;n para la
                    Ciencia y la Tecnolog&iacute;a (FCT) y cofinanciado por el Fondo Europeo de
                    Desarrollo Regional (FEDER), a trav&eacute;s de la Prioridad I del
                    Programa Operativo de los Factores de Competitividad (POFC) del Marco de Referencia Estrat&eacute;gica Nacional (QREN)&mdash;Uni&oacute;n Europea (COMPETE: FCOMP-01- 0124-FEDER-019715). Financiado
                    tambi&eacute;n por Fondos Nacionales a trav&eacute;s de la
                    Fundaci&oacute;n para la Ciencia y la Tecnolog&iacute;a en el
                    marco de los proyectos de &ldquo;Financiaci&oacute;n Plurianual&mdash;Unidad 759&rdquo;: &ldquo;PEst-OE/ELT/UI00759/2013&rdquo; y
                    &ldquo;PEst-OE/ELT/UI0759/2014&rdquo;.
                </p>
                    }
            </div>
            <div className="bottom-bar"></div>
        </div>
    )
}

export default Info