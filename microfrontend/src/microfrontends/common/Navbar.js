import React, { useEffect, useState, useCallback, useRef} from 'react'
import '../../resources/css/common/HomeNavbar.css'
import { Link, useHistory } from "react-router-dom";
import { connect } from 'react-redux';
import downArrow from '../../resources/assets/down-arrow.png'
import downArrowRed from '../../resources/assets/down-arrow-red.png'
import {ReactComponent as List} from '../../resources/assets/list.svg'
import temporaryLogo from '../../resources/assets/ReactUI.png'
import temporaryBeta from '../../resources/assets/beta.png'

const Navbar = (props) => {
    const history = useHistory()
    const [selected, setSelected] = useState(null)
    const [modulesPosition, setModulesPosition] = useState([])
    const [displayDropdown, setDisplayDropdown] = useState(true)
    const [displayControls, setDisplayControls] = useState(false)
    const [mobile, setMobile] = useState(false)
    const [showMobileContainer, setShowMobileContainer] = useState(false)
    const [moduleSet, setModuleSet] = useState([])

    const wrapperRef = useRef(null);
    useOutsideAlerter(wrapperRef);

    function useOutsideAlerter(ref) {
        useEffect(() => {
            function handleClickOutside(event) {
                if (ref.current && !ref.current.contains(event.target)) {
                    setDisplayDropdown(false)
                    setSelected(null)
                    setDisplayControls(false)
                }
            }
            document.addEventListener("mousedown", handleClickOutside);
            return () => {
                document.removeEventListener("mousedown", handleClickOutside);
            };
        }, [ref]);
    }


    useEffect(() => {
        resizeHandler()
        window.addEventListener("resize", () => {
            setDisplayDropdown(false)
            setSelected(null)
            resizeHandler()
        });

        return () => {
            window.removeEventListener("resize", () => {
                setDisplayDropdown(false)
                setSelected(null)
            });
         }       
         // eslint-disable-next-line react-hooks/exhaustive-deps 
    }, [])

    useEffect(() => {
        if(props.modules){
            var aux = props.modules
            var pages = [{id:"general_editor_prado", route:"/edition/acronym/JPC"},
                    {id:"general_editor_cunha", route:"/edition/acronym/TSC"},
                    {id:"general_editor_zenith", route:"/edition/acronym/RZ"},
                    {id:"general_editor_pizarro", route:"/edition/acronym/JP"},
                    {id:"header_title", route:"/edition/acronym/LdoD-Arquivo"}]
            for(let el of props.selectedVEAcr){
                pages.push({id:el, route:`/edition/acronym/${el}`, virtual:1})
            }
            aux[3].pages = pages
            setModuleSet(aux)
        }

    }, [props.modules, props.selectedVEAcr])

    function getWindowDimensions() {
        const { innerWidth: width, innerHeight: height } = window;
        return {
            width,
            height
        };
    }

    const resizeHandler = () => {
        let aux = getWindowDimensions()
        if(aux.width <= 768) setMobile(true)
        else setMobile(false)
    }

    const handleReroute = (val) => {
        setDisplayDropdown(false)
        setSelected(null)
        if(mobile) setShowMobileContainer(false)
        history.push(val)
    }
    const handleSelect = (sel) => {
        if(sel===selected){
            setSelected(null)
            setDisplayDropdown(false)
        }
        else{
            setSelected(sel)
            setDisplayDropdown(true)
        }
        
    }
    const callBackRef = useCallback(domNode => {
        if (domNode) {
            let arr = modulesPosition
            arr.push(domNode)
            setModulesPosition(arr)
        }
        
      }, [modulesPosition]);
      

    const handleModuleDisplay = () => {
        return moduleSet.map((mod,i) => {
            return(
                <div key={i} className="home-navbar-container-div" style={{display:mod.active?"flex":'none'}} ref={callBackRef}>
                    <p  className={selected===mod.name&&displayDropdown?"home-navbar-container-option-selected":"home-navbar-container-option"} 
                        onClick={() => handleSelect(mod.name)}
                        >
                        {props.messages[mod.name]}
                        <span><img alt="arrow" className="home-navbar-container-arrow" src={downArrow}></img></span>
                    </p>
                </div>
            )
        })
    }
    const handleDropdownPopulate = () => {
        return moduleSet.map((mod, i) => {
            return (
                <div key={i} style={{display:selected===mod.name&&displayDropdown?"block":'none', marginLeft:modulesPosition[i]?modulesPosition[i].offsetLeft:0}}>
                    {mod.pages.map((page,j)=> {
                        return(
                            page.id==="header_title"?
                            <p onClick={() => {handleReroute(page.route)}} key={j} className="dropdown-item-ldod">{props.messages[page.id]}</p>:
                            page.virtual?
                            <p onClick={() => {handleReroute(page.route)}} key={j} className="dropdown-item">{page.id}</p>:
                            page.id==="general_reading_visual"?
                            <a rel="noreferrer" href="https://ldod.uc.pt/ldod-visual" target="_blank" className="dropdown-item">{props.messages[page.id]}</a>:
                            <p onClick={() => {handleReroute(page.route)}} key={j} className="dropdown-item">{props.messages[page.id]}</p>
                        )
                    })}
                </div>
            )
        })
    }

    const handleAuthButtonClick = () => {
        if(!props.isAuthenticated) history.push(`/auth/signin`)
        else{
            setDisplayControls(!displayControls)
        }
    }
    const handleLogoutClick = () => {
        setDisplayControls(false)
        props.onLogout()
        history.push("/auth/signin")
    }
    return (
        <div className="home-navbar" ref={wrapperRef}>
            <div className="home-navbar-top">
                <div className="home-navbar-top-content">
                    <div style={{display:"flex"}}>
                        <Link className="home-navbar-top-content-title" to='/'>{props.messages.header_title}</Link>
                        <img alt="react-logo" className="react-logo" src={temporaryLogo}></img>
                        <img alt="react-beta" className="react-beta" src={temporaryBeta}></img>
                    </div>
                    <div className="home-navbar-top-content-auth" 
                        style={{color:props.isAuthenticated?"#FC1B27":"#000"}}
                        onClick={() => handleAuthButtonClick()}>
                        {props.isAuthenticated?
                            <div style={{display:"flex", alignItems:"center"}}>
                                <p>{props.currentUser.firstName} {props.currentUser.lastName}</p>
                                <img alt="arrow" className="home-navbar-container-arrow" src={downArrowRed}></img>
                            </div>
                        :<p className="home-navbar-login">{props.messages.header_login}</p>
                        }
                        {
                            displayControls?
                                <div className="home-navbar-controls">
                                    <p className="home-navbar-controls-text" onClick={() => history.push("/auth/changePassword")}>{props.messages.user_password}</p>
                                    <p className="home-navbar-controls-text" style={{marginTop:"5px"}}
                                        onClick={() => handleLogoutClick()}>{props.messages.header_logout}</p>
                                </div>
                            :null
                        }
                    </div>
                    <List onClick={() => setShowMobileContainer(!showMobileContainer)} className="home-navbar-list"></List>
                </div>
            </div>
            {
                !mobile?
                <div>
                    <div className="home-navbar-container">
                        {handleModuleDisplay()}
                        <div className="home-navbar-language">
                            <p onClick={() => props.setLanguage("pt")} className="home-navbar-language-option" style={props.language==="pt"?{color:"#FC1B27"}:{color:"black"}}>PT</p>
                            <p onClick={() => props.setLanguage("en")} className="home-navbar-language-option" style={props.language==="en"?{color:"#FC1B27"}:{color:"black"}}>EN</p>
                            <p onClick={() => props.setLanguage("es")} className="home-navbar-language-option" style={props.language==="es"?{color:"#FC1B27"}:{color:"black"}}>ES</p>
                        </div>
                    </div>
                    <div style={{display:selected!==null&&displayDropdown?"inline":'none'}} className="home-navbar-dropdown-div">
                        {handleDropdownPopulate()}
                    </div>
                </div>
                :null
            }
            {
                mobile&&showMobileContainer?
                <div>
                    <div className="home-navbar-container-mobile">
                        {handleModuleDisplay()}
                        <div className="home-navbar-top-content-auth-mobile" 
                                style={{color:props.isAuthenticated?"#FC1B27":"#000"}}
                                onClick={() => handleAuthButtonClick()}>
                                {props.isAuthenticated?
                                    <div style={{display:"flex", alignItems:"center"}}>
                                        <p style={{cursor:"pointer"}}>{props.currentUser.firstName} {props.currentUser.lastName}</p>
                                        <img alt="arrow" className="home-navbar-container-arrow" src={downArrowRed}></img>
                                    </div>
                                :<p className="home-navbar-login">{props.messages.header_login}</p>
                                }
                                {
                                    displayControls?
                                        <div className="home-navbar-controls">
                                            <p className="home-navbar-controls-text" onClick={() => history.push("/auth/changePassword")}>{props.messages.user_password}</p>
                                            <p className="home-navbar-controls-text" style={{marginTop:"5px"}}
                                                onClick={() => handleLogoutClick()}>{props.messages.header_logout}</p>
                                        </div>
                                    :null
                                }
                            </div>
                        <div className="home-navbar-language">
                            <p onClick={() => props.setLanguage("pt")} className="home-navbar-language-option" style={props.language==="pt"?{color:"#FC1B27"}:{color:"black"}}>PT</p>
                            <p onClick={() => props.setLanguage("en")} className="home-navbar-language-option" style={props.language==="en"?{color:"#FC1B27"}:{color:"black"}}>EN</p>
                            <p onClick={() => props.setLanguage("es")} className="home-navbar-language-option" style={props.language==="es"?{color:"#FC1B27"}:{color:"black"}}>ES</p>
                        </div>
                    </div>
                    <div style={{display:selected!==null&&displayDropdown?"inline":'none'}} className="home-navbar-dropdown-div">
                        {handleDropdownPopulate()}
                    </div>
                </div>
                :null
            }

            
            
        </div>
    )
}

const mapStateToProps = (state) => {
    return {
        modules:state.modules,
        selectedVEAcr: state.selectedVEAcr
    }
}
export default connect(mapStateToProps,null)(Navbar) 