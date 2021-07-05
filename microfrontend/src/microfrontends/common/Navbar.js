import React, { useEffect, useState, useCallback, useRef} from 'react'
import '../../resources/css/home/Navbar.css'
import { Link, useHistory } from "react-router-dom";
import { connect } from 'react-redux';
import downArrow from '../../resources/assets/down-arrow.png'


const Navbar = (props) => {
    const history = useHistory()
    const [selected, setSelected] = useState(null)
    const [modulesPosition, setModulesPosition] = useState([])
    const [displayDropdown, setDisplayDropdown] = useState(true)
    const wrapperRef = useRef(null);
    useOutsideAlerter(wrapperRef);

    function useOutsideAlerter(ref) {
        useEffect(() => {
            function handleClickOutside(event) {
                if (ref.current && !ref.current.contains(event.target)) {
                    setDisplayDropdown(false)
                    setSelected(null)
                }
            }
            document.addEventListener("mousedown", handleClickOutside);
            return () => {
                document.removeEventListener("mousedown", handleClickOutside);
            };
        }, [ref]);
    }


    useEffect(() => {
        window.addEventListener("resize", () => {
            setDisplayDropdown(false)
            setSelected(null)
        });

        return () => {
            window.removeEventListener("resize", () => {
                setDisplayDropdown(false)
                setSelected(null)
            });
         };
    }, [])

    const handleReroute = (val) => {
        setDisplayDropdown(false)
        setSelected(null)
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
        
      }, []);
      

    const handleModuleDisplay = () => {
        return props.modules.map((mod,i) => {
            return(
                <div key={i} className="home-navbar-container-div" style={{backgroundColor:selected===mod.name&&displayDropdown?"#e7e7e7":"white", display:mod.active?"flex":'none'}} ref={callBackRef}>
                    <p  className={selected===mod.name&&displayDropdown?"home-navbar-container-option-selected":"home-navbar-container-option"} 
                        onClick={() => handleSelect(mod.name)}
                        >
                        {props.messages[mod.name]}
                        <span><img className="home-navbar-container-arrow" src={downArrow}></img></span>
                    </p>
                </div>
            )
        })
    }
    const handleDropdownPopulate = () => {
        return props.modules.map((mod, i) => {
            return (
                <div key={i} style={{display:selected===mod.name&&displayDropdown?"block":'none', marginLeft:modulesPosition[i]?modulesPosition[i].offsetLeft:0}}>
                    {mod.pages.map((page,i)=> {
                        return(
                            page.id==="header_title"?
                            <p onClick={() => {handleReroute(page.route)}} key={i} className="dropdown-item-ldod">{props.messages[page.id]}</p>:
                            <p onClick={() => {handleReroute(page.route)}} key={i} className="dropdown-item">{props.messages[page.id]}</p>
                        )
                    })}
                </div>)
        })
    }
    return (
        <div className="home-navbar" ref={wrapperRef}>
            <div className="home-navbar-top">
                <div className="home-navbar-top-content">
                    <Link className="home-navbar-top-content-title" to='/'>{props.messages.header_title}</Link>
                    <Link className="home-navbar-top-content-auth" onClick={props.isAuthenticated?props.onLogout:null} to={props.isAuthenticated?'/':'/auth/signin'}>{props.isAuthenticated?props.currentUser.username:props.messages.header_login}</Link>
                </div>
            </div>
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
    )
}

const mapStateToProps = (state) => {
    return {
        modules:state.modules   
    }
}
export default connect(mapStateToProps,null)(Navbar) 