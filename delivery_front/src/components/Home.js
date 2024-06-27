import React from 'react'
import axios from 'axios';
import { useState, useEffect } from 'react';
import classes from './Home.module.css'
import  {Link, Routes, Route, useLocation, useNavigate} from 'react-router-dom'
import Detail from './Detail'
import Profile from './Profile';
import Register from './Register';
import Login from './Login';
import Order from './Order';


const Home = ({}) => {

        const [loading, setLoading] = useState(true);
        const [data, setData] = useState(null);
        const [profile, setProfile] = useState();

        const handleProfile = (data) => {
            setProfile(data);
        }
        const navigate = useNavigate();


        useEffect(() => {
            axios.get("http://localhost:8080/api/v1/restaurants").then((response) => {
            setData(response.data);
            setLoading(false);
            });
        }, []);

    return (
        <>
        
        {
            loading ? (
                console.log("Loading..")
              ) : (
                data.map((item) => (
                    <Routes>
                        <Route path={`${item.name}/*`} element={<Detail id={item.id} profile={profile}/>}/>
                    </Routes>
                ))
            )
        }
        
        {
            
            loading ? (
                console.log("Loading..")
              ) : (
                <>
                {
                    profile == null ? (
                    <div >
                        <Routes>
                        <Route path='register' element={<Register/>}/>
                        <Route path='login' element={<Login profile={handleProfile}/>}/>
                        </Routes>
                    <div className={classes.container}>
                        <Link to="login"><button className={classes.btn}>Sign Up</button></Link>
                        <Link to="register"><button className={classes.btn}>Register</button></Link>
                    </div>
                    </div>) :
                    (
                    <>
                    <Routes>
                        <Route path='profile' element={<Profile id={profile.id}/>}/>
                    </Routes>
                    <Routes>
                        <Route path='orders' element={<Order id={profile.id}/>}/>
                    </Routes>
                    <Link to="profile"><button className={classes.btn}>Profile</button></Link>
                    <button onClick={() => {setProfile(null), navigate("/")}} className={classes.btn}>Signout</button>
                    {
                        profile.role == "courier" && (
                            <Link to="orders"><button className={classes.btn}>Orders</button></Link>
                        )
                    }
                    </>
                )
                    
                }
                <div className={classes.flexbox}>
                    {
                        data.map((item, index) => (
                                                
                            <div className={classes.restaurants}>
                                <Link to={`${item.name}`}>
                                    <img key={index} src={item.picture} alt='picture' width="200" height="200"/>
                                </Link>
                                <p>{item.name}</p>           
                            </div>
                        ))
                    }                   
                </div>
                
                
                </>
                
            )
        }
        
        </>
    )
}
export default Home