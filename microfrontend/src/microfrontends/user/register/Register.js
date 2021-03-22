import React, { useState } from 'react'
import { 
    NAME_MIN_LENGTH, NAME_MAX_LENGTH, 
    USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH,
    EMAIL_MAX_LENGTH,
    PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH
} from '../../../constants/index.js';
import { useHistory } from "react-router-dom";
import '../../../resources/css/user/AuthenticationRegister.css'
import { signup } from '../../../util/utilsAPI';


const AuthenticationRegister = () => {

    const history = useHistory()

    const [name, setName] = useState('lul2as')
    const [surname, setSurrname] = useState('rep2py')
    const [username, setUsername] = useState('lu2laswag21')
    const [email, setEmail] = useState('lu2la22s@gmail.com')
    const [password, setPassword] = useState('password')

    const validateDetails = (request) => {
        if(request.name.length>NAME_MIN_LENGTH && request.name.length< NAME_MAX_LENGTH &&
            request.username.length>USERNAME_MIN_LENGTH && request.username.length<USERNAME_MAX_LENGTH &&
                request.email.length<EMAIL_MAX_LENGTH &&
                    request.username.length>PASSWORD_MIN_LENGTH && request.username.length<PASSWORD_MAX_LENGTH){
                        return true
                    }
                    else return false
    }

    //AUTHENTICATION METHODS
    const handleRegister = () => {
        const signupRequest = {
            name: name+' '+surname,
            email: email,
            username: username,
            password: password
        }
        if(validateDetails(signupRequest)){
            signup(signupRequest) //API COMMUNICATION
            .then(res => {
                console.log(res.data);
                history.push("/auth/signin")
            })
            .catch(error => {
                console.log(error)
                if(error.status === 401) {
                    console.log(error)  
                    console.log(401)      
                } else {
                    console.log(error)                                        
                }
            });
        }
        
    }
    ///

    return(
        <div className="registo">
            <p className="registo-title">Registo</p>
            <div className="registo-input-div">
                <div className="registo-input-div-flex"> 
                    <p className="registo-input-name">Primeiro nome</p>
                    <input className="registo-input-input" onChange={e => setName(e.target.value)}></input>
                </div>
                <div className="registo-input-div-flex">
                    <p className="registo-input-name">Apelido</p>
                    <input className="registo-input-input" onChange={e => setSurrname(e.target.value)}></input>
                </div>
                <div className="registo-input-div-flex">
                    <p className="registo-input-name">Nome de utilizador</p>
                    <input className="registo-input-input" onChange={e => setUsername(e.target.value)}></input>
                </div>
                <div className="registo-input-div-flex">
                    <p className="registo-input-name">Senha</p>
                    <input className="registo-input-input" onChange={e => setEmail(e.target.value)}></input>
                </div>
                <div className="registo-input-div-flex">
                    <p className="registo-input-name">Endereço eletrónico</p>
                    <input className="registo-input-input" onChange={e => setPassword(e.target.value)} type="password"></input>
                </div>
                <div className="registo-input-div-flex">
                    <p className="registo-input-name">Código de Conduta</p>
                    <div className="registo-input-conduta">
                        <input type="checkbox" className="registo-input-conduta-select"></input>
                        <p className="registo-input-conduta-text">Ao registar-me como utilizador do Arquivo LdoD, aceito o código de conduta abaixo</p>
                    </div>
                </div>
            </div>
            <div className="registo-text">
                <p>As funcionalidades de edição virtual e de escrita virtual são parte integrante das camadas dinâmicas do ambiente textual 
                    colaborativo do Arquivo LdoD. Destinam-se a ser usadas em práticas de aprendizagem, investigação e criação. 
                    Os utilizadores registados podem criar, anotar e publicar suas próprias edições virtuais livremente. 
                    Quando a funcionalidade de escrita virtual estiver disponível, poderão também escrever e publicar variações com base 
                    em referências textuais específicas do Livro do Desassossego. Todos os utilizadores registados no Arquivo LdoD devem 
                    respeitar as regras de conduta definidas neste documento.</p>
                <p style={{marginTop:"30px"}}>Ao registar-se como utilizador do Arquivo LdoD, concorda em não usar a plataforma para:</p>
                <ol>
                    <li>publicar qualquer conteúdo que seja ilegal, doloso, insultuoso, difamatório, obsceno, invasivo 
                        da privacidade de outrem, de incitamento ao ódio, ou cujo teor desrespeite direitos sexuais e 
                        de género, assim como direitos étnicos e raciais.</li>
                    <li>publicar qualquer conteúdo que não tenha o direito de disponibilizar de acordo com a lei ou 
                        de acordo com disposições contratuais específicas.</li>
                    <li>publicar qualquer conteúdo que viole qualquer patente, marca registada, direitos autorais 
                        ou outros direitos de propriedade de terceiros.</li>
                    <li>publicar qualquer conteúdo publicitário, materiais promocionais, “lixo eletrónico” (“spam”)
                         ou qualquer outra forma de solicitação.</li>
                    <li>publicar qualquer material que contenha vírus ou qualquer outro código de computador, 
                        ficheiros ou programas concebidos para interromper, destruir ou limitar a funcionalidade 
                        de qualquer software ou hardware ou equipamento de telecomunicações.</li>
                    <li>interferir com ou interromper o site ou servidores ou redes ligados ao site, ou infringir 
                        quaisquer requisitos, procedimentos, políticas ou regulamentos das redes ligadas ao site.</li>
                    <li>recolher ou armazenar dados pessoais sobre outros utilizadores.</li>
                </ol>
            </div>
            <button className="registo-button" onClick={() => handleRegister()}>Registo</button>
            
        </div>
    )

}

export default AuthenticationRegister