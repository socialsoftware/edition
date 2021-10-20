import React from 'react'
import '../../resources/css/common/Home.css'
import {useHistory} from 'react-router-dom'
import Info from './Info'

const Home = (props) => {
    const history = useHistory()
    const excerpts =   [["Jerónimo Pizarro","E eu offereço-te este livro porque sei que elle é bello e inutil.","17","Fr449/inter/Fr449_WIT_ED_CRIT_P"],
                    ["Jacinto do Prado Coelho","Senti-me agora respirar como se houvesse practicado uma cousa nova, ou atrazada.","188","Fr157/inter/Fr157_WIT_ED_CRIT_C"],
                    ["Teresa Sobral Cunha","Em mim foi sempre menor a intensidade das sensações que a intensidade da sensação delas.","283","Fr309/inter/Fr309_WIT_ED_CRIT_SC"],
                    ["Richard Zenith","O silêncio que sai do som da chuva espalha-se, num crescendo de monotonia cinzenta, pela rua estreita que fito.","41","Fr175/inter/Fr175_WIT_ED_CRIT_Z"],
                    ["Jerónimo Pizarro","A grande terra, que serve os mortos, serviria, menos maternalmente, esses papeis.","387","Fr159.b/inter/Fr159_b_WIT_ED_CRIT_P_1"],
                    ["Teresa Sobral Cunha","Em cada pingo de chuva a minha vida falhada chora na natureza.","266","Fr390/inter/Fr390_WIT_ED_CRIT_SC"],
                    ["Jacinto do Prado Coelho","Como nos dias em que a trovoada se prepara e os ruidos da rua fallam alto com uma voz solitária.","45","Fr042/inter/Fr042_WIT_ED_CRIT_C"],
                    ["Teresa Sobral Cunha","Ninguém estava quem era, e o patrão Vasques apareceu à porta do gabinete para pensar em dizer qualquer coisa.","441","Fr043/inter/Fr043_WIT_ED_CRIT_SC"],
                    ["Richard Zenith","'Vem aí uma grande trovoada', disse o Moreira, e voltou a página do Razão.","183","Fr044/inter/Fr044_WIT_ED_CRIT_Z"],
                    ["Jerónimo Pizarro","E então, em plena vida, é que o sonho tem grandes cinemas.","262","Fr149/inter/Fr149_WIT_ED_CRIT_P"],
                    ["Jerónimo Pizarro","Lêr é sonhar pela mão de outrem.","586","Fr554/inter/Fr554_WIT_ED_CRIT_P"],
                    ["Jacinto do Prado Coelho","Devo ao ser guarda-livros grande parte do que posso sentir e pensar como a negação e a fuga do cargo.","133","Fr198/inter/Fr198_WIT_ED_CRIT_C"],
                    ["Teresa Sobral Cunha","Durmo sobre os cotovelos onde o corrimão me doe, e sei de nada como um grande prometimento.","380","Fr030/inter/Fr030_WIT_ED_CRIT_SC"],
                    ["Richard Zenith","Sentado á janela, contemplo com os sentidos todos esta coisa nenhuma da vida universal que está lá fora.","50","Fr118/inter/Fr118_WIT_ED_CRIT_Z"],
                    ["Jerónimo Pizarro","Já me cansa a rua, mas não, não me cansa - tudo é rua na vida.","284","Fr523/inter/Fr523_WIT_ED_CRIT_P"],
                    ["Jacinto do Prado Coelho","Mergulhou na sombra como quem entra na porta onde chega.","485","Fr306a/inter/Fr306a_WIT_ED_CRIT_C"],
                    ["Jacinto do Prado Coelho","Para mim os pormenores são coisas, vozes, lettras.","163","Fr255/inter/Fr255_WIT_ED_CRIT_C"],
                    ["Teresa Sobral Cunha","Entre mim e a vida há um vidro ténue.","171","Fr447/inter/Fr447_WIT_ED_CRIT_SC"],
                    ["Richard Zenith","Não toquemos na vida nem com as pontas dos dedos.","284","Fr452/inter/Fr452_WIT_ED_CRIT_Z"],
                    ["Jerónimo Pizarro","Não era isto, porém, que eu queria dizer.","394","Fr264/inter/Fr264_WIT_ED_CRIT_P"],
                    ["Jacinto do Prado Coelho","Minha alma está hoje triste até ao corpo.","167","Fr269/inter/Fr269_WIT_ED_CRIT_C"],
                    ["Jacinto do Prado Coelho","Eu não sei quem tu és, mas sei ao certo o que sou?","254","Fr285/inter/Fr285_WIT_ED_CRIT_C"],
                    ["Teresa Sobral Cunha","Pasmo sempre quando acabo qualquer coisa.","711","Fr009/inter/Fr009_WIT_ED_CRIT_SC"],
                    ["Richard Zenith","É uma oleografia sem remédio.","25","Fr010/inter/Fr010_WIT_ED_CRIT_Z"],
                    ["Jerónimo Pizarro","Toda a vida é um somno.","197","Fr027/inter/Fr027_WIT_ED_CRIT_P"],
                    ["Richard Zenith","Não consegui nunca ver-me de fora.","338","Fr028/inter/Fr028_WIT_ED_CRIT_Z"],
                    ["Jacinto do Prado Coelho","Jogar ás escondidas com a nossa consciencia de viver.","370","Fr437/inter/Fr437_WIT_ED_CRIT_C"],
                    ["Teresa Sobral Cunha","A arte livra-nos ilusoriamente da sordidez de sermos.","456","Fr163/inter/Fr163_WIT_ED_CRIT_SC"],
                    ["Richard Zenith","As coisas sonhadas só têm o lado de cá.","346","Fr510/inter/Fr510_WIT_ED_CRIT_Z"],
                    ["Jerónimo Pizarro","Sou uma placa photographica prolixamente impressionavel.","59","Fr456/inter/Fr456_WIT_ED_CRIT_P"]];
    
    const excerptID = Math.round((Math.random() * 29)) 
    const img1 =  Math.floor((Math.random() * 2)+1)
    const img2 =  Math.floor((Math.random() * 2)+1)
    const img3 =  Math.floor((Math.random() * 2)+1)
    const img4 =  Math.floor((Math.random() * 2)+1)
    const img5 =  Math.floor((Math.random() * 2)+1)

    return (
        <div className="home">
            <div className="home-container">
                <div className="home-frag-excerpt" onClick={() => {history.push(`/reading/fragment/${excerpts[excerptID][3]}`)}}>
                    <p className="home-frag-number">{excerpts[excerptID][2]}</p>
                    <p className="home-frag-editor">{excerpts[excerptID][0]}</p>
                </div>
                <p className="home-frag-excerpt-text">{excerpts[excerptID][1]}</p>
                <hr className="home-line-points"></hr>
                {props.language==="pt"?
                <p className="home-ldod-text">
                    O Arquivo LdoD é um arquivo digital colaborativo do <span className="s-ws">Livro do Desassossego</span> de <span className="s-ws">Fernando Pessoa</span>.
                    Contém <span className="s-underl">imagens</span> dos documentos autógrafos, <span className="s-underl">novas transcrições</span>
                    desses documentos e ainda transcrições de <span className="s-underl">quatro edições da obra</span>.
                    Além da <span className="s-underl">leitura</span> e <span className="s-underl">comparação</span> das transcrições, o Arquivo LdoD permite que os
                    utilizadores colaborem na criação de <span className="s-underl">edições virtuais</span> do Livro do Desassossego.
                </p>
                :props.language==="en"?
                <p className="home-ldod-text">
                The LdoD Archive is a collaborative digital archive of the Book of Disquiet by Fernando Pessoa. It contains images of the autograph documents, new transcriptions of those documents and also transcriptions of four editions of the work. In addition to reading and comparing transcriptions, the LdoD Archive enables users to collaborate in creating virtual editions of the Book of Disquiet.
                </p>
                :
                <p className="home-ldod-text">
                El Archivo LdoD es un archivo digital colaborativo del Libro del desasosiego de Fernando Pessoa. Contiene imágenes de los documentos originales, nuevas transcripciones de estos documentos y transcripciones de cuatro ediciones de la obra. Además de la lectura y la comparación de las transcripciones, el Archivo LdoD permite a los usuarios colaborar en la creación de ediciones virtuales del Libro del desasosiego.
                </p>
                }
                <hr className="home-line-x"></hr>
                <div className="home-boxes-web">
                    <div onClick={() => {history.push("/reading")}} className="home-div-link">
                        <img className="home-img" src={require(`../../resources/assets/boxes/D-${props.language}-01-${img1}.svg`).default} alt="img1"></img>
                        <img className="home-img-hover" src={require(`../../resources/assets/boxes/D-${props.language}-01-${img1}-h.svg`).default} alt="img1"></img>
                    </div>
                    <hr className="line-points"></hr>
                    <div onClick={() => {history.push("/documents/source/list")}} className="home-div-link">
                        <img className="home-img" src={require(`../../resources/assets/boxes/D-${props.language}-02-${img2}.svg`).default} alt="img1"></img>
                        <img className="home-img-hover" src={require(`../../resources/assets/boxes/D-${props.language}-02-${img2}-h.svg`).default} alt="img1"></img>
                    </div>
                    <hr className="line-points"></hr>
                    <div onClick={() => {history.push("/edition")}} className="home-div-link">
                        <img className="home-img" src={require(`../../resources/assets/boxes/D-${props.language}-03-${img3}.svg`).default} alt="img1"></img>
                        <img className="home-img-hover" src={require(`../../resources/assets/boxes/D-${props.language}-03-${img3}-h.svg`).default} alt="img1"></img>
                    </div>
                    <hr className="line-points"></hr>
                    <div onClick={() => {history.push("/search/simple")}} className="home-div-link">
                        <img className="home-img" src={require(`../../resources/assets/boxes/D-${props.language}-04-${img4}.svg`).default} alt="img1"></img>
                        <img className="home-img-hover" src={require(`../../resources/assets/boxes/D-${props.language}-04-${img4}-h.svg`).default} alt="img1"></img>
                    </div>
                    <hr className="line-points"></hr>
                    <div onClick={() => {history.push("/virtual/virtualeditions")}} className="home-div-link">
                        <img className="home-img" src={require(`../../resources/assets/boxes/D-${props.language}-05-${img5}.svg`).default} alt="img1"></img>
                        <img className="home-img-hover" src={require(`../../resources/assets/boxes/D-${props.language}-05-${img5}-h.svg`).default} alt="img1"></img>
                    </div>
                </div>
                <div className="home-boxes-mobile">
                    <div onClick={() => {history.push("/reading")}} className="home-div-link">
                        <img className="home-img" src={require(`../../resources/assets/boxes/M-${props.language}-01-${img1}.svg`).default} alt="img1"></img>
                        <img className="home-img-hover" src={require(`../../resources/assets/boxes/M-${props.language}-01-${img1}-h.svg`).default} alt="img1"></img>
                    </div>
                    <hr className="home-line-points"></hr>
                    <div onClick={() => {history.push("/documents/source/list")}} className="home-div-link">
                        <img className="home-img" src={require(`../../resources/assets/boxes/M-${props.language}-02-${img2}.svg`).default} alt="img1"></img>
                        <img className="home-img-hover" src={require(`../../resources/assets/boxes/M-${props.language}-02-${img2}-h.svg`).default} alt="img1"></img>
                    </div>
                    <hr className="home-line-points"></hr>
                    <div onClick={() => {history.push("/edition")}} className="home-div-link">
                        <img className="home-img" src={require(`../../resources/assets/boxes/M-${props.language}-03-${img3}.svg`).default} alt="img1"></img>
                        <img className="home-img-hover" src={require(`../../resources/assets/boxes/M-${props.language}-03-${img3}-h.svg`).default} alt="img1"></img>
                    </div>
                    <hr className="home-line-points"></hr>
                    <div onClick={() => {history.push("/search/simple")}} className="home-div-link">
                        <img className="home-img" src={require(`../../resources/assets/boxes/M-${props.language}-04-${img4}.svg`).default} alt="img1"></img>
                        <img className="home-img-hover" src={require(`../../resources/assets/boxes/M-${props.language}-04-${img4}-h.svg`).default} alt="img1"></img>
                    </div>
                    <hr className="home-line-points"></hr>
                    <div onClick={() => {history.push("/virtual/virtualeditions")}} className="home-div-link">
                        <img className="home-img" src={require(`../../resources/assets/boxes/M-${props.language}-05-${img5}.svg`).default} alt="img1"></img>
                        <img className="home-img-hover" src={require(`../../resources/assets/boxes/M-${props.language}-05-${img5}-h.svg`).default} alt="img1"></img>
                    </div>
                </div>
                
            </div>
            <Info/>
            
        </div>  
    )
}

export default Home