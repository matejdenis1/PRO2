import {Link, Routes, Route, useNavigate, Navigate, useAsyncValue} from 'react-router-dom'
import axios from 'axios'
import { useEffect, useState } from 'react';

function Login({loginState, profile}) {
    const [loading, setLoading] = useState(true);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const navigate = useNavigate();

    useEffect(() => {
        setLoading(false);
    }, [])

    const validateForm = () => {
        if (!email || !password) {
            setError('All fields are required');
            return false;
        }
        setError('');
        return true;
      };
      
    async function login(event) {
        event.preventDefault();
        if(validateForm){
            try {
                const res = await axios.post("http://localhost:8080/api/v1/users/login", {
                    email: email,
                    password: password,
                });
        
                if (res.data) {
                    console.log("Logged");
                    profile(res.data);
                    navigate("/");
                } else {
                    console.log("Not logged");
                }
            } catch (err) {
                if (err.response && err.response.status === 401) {
                    setError('Incorrect credentials');
                } else {
                    alert(err);
                }
            }
        }
        
    }

    return (
        <>
        {
            
            loading ? (
                console.log("Loading..")
              ) : (
                <div>
            <div class="container">
            <div class="row">
                <h2>Login</h2>
             <hr/>
             </div>
             {error && <p style={{ color: 'red' }}>{error}</p>}
             <div class="row">
             <div class="col-sm-6">
 
            <form>
        <div class="form-group">
          <label>Email</label>
          <input type="email"  class="form-control" id="email" placeholder="Enter Name"
          
          value={email}
          onChange={(event) => {
            setEmail(event.target.value);
          }}
          
          />
        </div>
        <div class="form-group">
            <label>Password</label>
            <input type="password"  class="form-control" id="password" placeholder="Enter Password"
            
            value={password}
            onChange={(event) => {
              setPassword(event.target.value);
            }}
            
            />
          </div>
                  <button type="submit" class="btn btn-primary" onClick={login} >Login</button>
              </form>
            </div>
            </div>
            </div>
     </div> 
                
            )
        }
        
        </>
    );
}

export default Login;