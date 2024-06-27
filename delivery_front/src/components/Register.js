import { useNavigate } from 'react-router-dom';
import axios from 'axios'
import { useEffect, useState } from 'react';

function Register() {
      
    const [loading, setLoading] = useState(true);
    const [email, setEmail] = useState('');
    const [address, setAddress] = useState('');
    const [password, setPassword] = useState('');
    const [repassword, setRepassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
      setLoading(false);
  }, [])
    

    const validateForm = () => {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!email || !address || !password || !repassword) {
          setError('All fields are required');
          return false;
      }
      if (!emailRegex.test(email)) {
        setError('Invalid email format');
        return false;
    }
      if (password !== repassword) {
          setError('Passwords do not match');
          return false;
      }
      setError('');
      return true;
    };

    async function register(event){
        event.preventDefault();
        if(validateForm()){
          try{
            await axios.post("http://localhost:8080/api/v1/users/register", {
            email: email,
            password: password,
            address: address,
            }).then((res) => {
                if(res.data != ""){
                    setError(res.data)               
                }
                else{
                  console.log("Registered")
                  navigate("/");
                }
            }
        )} catch (error) {
          console.error('Error registering user', error);
          setError('Registration failed');
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
                <h2>Register</h2>
             <hr/>
             </div>
             {error && <p style={{ color: 'red' }}>{error}</p>}
             <div class="row">
             <div class="col-sm-6">
 
            <form>
        <div class="form-group">
          <label>Email</label>
          <input type="email"  class="form-control" id="email" placeholder="Enter Email"
          
          value={email}
          onChange={(event) => {
            setEmail(event.target.value);
          }}
          
          />
        </div>
        <div class="form-group">
            <label>Address</label>
            <input type="address"  class="form-control" id="address" placeholder="Enter Address"
            
            value={address}
            onChange={(event) => {
              setAddress(event.target.value);
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
          
          <div class="form-group">
            <label>Re-Password</label>
            <input type="password"  class="form-control" id="repassword" placeholder="Enter Password Again"
            value={repassword}
            onChange={(event) => {
                setRepassword(event.target.value);
            }}
            
            />
          </div>
            
            
          <button type="submit" class="btn btn-primary" onClick={register} >Register</button>
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

export default Register;