import axios from 'axios'
import {useState, useEffect} from 'react'
import classes from "./Profile.module.css"
import {useNavigate} from 'react-router-dom'

const Courier = ({user}) =>{
    const [loading, setLoading] = useState(true);
    const [data, setData] = useState(null);
    const [refresh, setRefresh] = useState(false);
    useEffect(() => {
        axios.get("http://localhost:8080/api/v1/orders/deliveres").then((response) => {
        setData(response.data);    
        setLoading(false);    
        });
        
        
    }, [refresh]);
    const navigate = useNavigate();

    async function updateOrder(event, orderId){
        event.preventDefault();
        
        await axios.post("http://localhost:8080/api/v1/orders/update", 
            { 
                courierId: user.id,
                orderId: orderId
            }).then((res) => {
            if(res.data != null){
                console.log("Order updated!")
                setRefresh(prev => !prev)
            }
            else{
                console.log("Order not updated!")
            }
        })
    }

    return(
        <>
        {
            loading ? (
                console.log("Loading")) :
            (
                data.length > 0 ? 
                (
                    data.map(order => (
                        <div className={classes.orderDetails}>
                            <p>Customer email: {order.customer.email}</p>
                            <p>Customer address: {order.customer.address}</p>
                            <p>Restaurant name: {order.restaurant.name}</p>
                            <p>Restaurant address: {order.restaurant.address}</p>
                            {
                                order.menu.map(element => (
                                    <p>{element.name} {element.price} € - x {element.amount} </p>
                                ))
                            }
                            <p>Order price: {order.price}</p>                                                                  
                            <button onClick={(event) => updateOrder(event, order.id)}>Take Order</button>
                        </div>))
                )
                : 
                (
                    <p style={{ color: 'red' }}>There are no orders to take!</p>
                )
                
            )
            
        }
        </>
    );

}
export default Courier;