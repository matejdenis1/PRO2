import axios from 'axios'
import {useState, useEffect} from 'react'
import classes from './Profile.module.css'
import Courier from './Courier';
import Customer from './Customer';
function Profile({id}) {

    const [loading, setLoading] = useState(true);
    const [user, setUser] = useState(null);
    

    useEffect(() => {
        axios.get("http://localhost:8080/api/v1/users/" + id).then((response) => {
        setUser(response.data);
        setLoading(false); 
        });
        
    }, []);

        return (
            <>
                {loading ? (
                    console.log("Loading..")
                ) : (
                    <>
                    {
                        user.role == "courier" && <Courier user={user}/>
                    }
                    {
                        user.role == "customer" && <Customer user={user}/>
                    }
                    </>
                )}
            </>
        );
}

export default Profile;