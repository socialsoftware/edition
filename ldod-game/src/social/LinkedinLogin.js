import React, { Component } from 'react';
import { notification } from 'antd';
import config from './Config';
import { ACCESS_TOKEN } from '../utils/Constants';
import FontAwesome from 'react-fontawesome'

class LinkedinLogin extends Component{
    
    render(){
        return(
            <div className="row">
                <div className="col-md-offset-5 col-md-2">
               {/*<button onClick={() => console.log(localStorage.getItem(ACCESS_TOKEN))}/>*/}
                    <button className="btn btn-block btn-social btn-linkedin" type="submit">
                        <span>
                            <FontAwesome className='linkedin'name='linkedin'/>
                        </span>Linkedin
                    </button>				
                </div>
            </div>
        )
    }
}

export default LinkedinLogin;