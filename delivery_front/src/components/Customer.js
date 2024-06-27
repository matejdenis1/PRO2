import axios from 'axios'
import {useState, useEffect} from 'react'
import classes from "./Profile.module.css"
const Customer = ({user}) => {
    const [loading, setLoading] = useState(true);
    const [data, setData] = useState(null);
    const [ratings, setRatings] = useState(null);
    const [ratedRestaurants, setRatedRestaurants] = useState([]);
    const [error, setError] = useState('');

    const handleInputChange = (event, restaurantId) => {
        const { value } = event.target;
        setRatings({ ...ratings, [restaurantId]: value });
    };

    
    useEffect(() => {
        axios.get("http://localhost:8080/api/v1/orders/customerOrders/" + user.id).then((response) => {
        setData(response.data);
        setRatedRestaurants(user.ratedRestaurants);
        setLoading(false);
        });
        
        
    }, []);
    const validate = (rating) => {
        if(rating == ""){
            setError("Rating must be filled!")
            return false
        }
        if(rating < 1 || rating > 5){
            setError("Rating must be 1-5")
            return false
        }
        return true
    } 

    async function rateRestaurant(event, restaurantId){
        event.preventDefault;
        const rating = ratings[restaurantId];
        if(validate(rating)){
            try{
                await axios.post("http://localhost:8080/api/v1/orders/increment-rating",
                    {
                        customerId: user.id,
                        restaurantId: restaurantId,
                        rating: rating
                    }
                ).then((res) => {
                    if(res.data != null){
                        console.log("Rating updated!")
                        setRatedRestaurants([...ratedRestaurants, restaurantId]);
                    }
                    else{
                        console.log("Rating not updated!")
                    }
                })
            }
            catch(err){
                alert(err);
            }
        }
    }

    return(
        <>
        {
            loading ? (console.log("Loading")) : (

                data.length > 0 ? (
                    data.map(order => (
                        <div className={classes.orderDetails}>
                            <p>Delivery address: {order.customer.address}</p>
                            <p>Restaurant name: {order.restaurant.name}</p>
                            <p>Restaurant address: {order.restaurant.address}</p>
    
                            {
                                order.menu.map(element => (
                                    <p>{element.name} {element.price} € - x {element.amount} </p>
                                ))
                            }
                            <p>Order price: {order.price}</p> 
                            {
                            ratedRestaurants.includes(order.restaurant.id) ? (
                                <p>Thank you for your rating!</p>
                            ) : (
                                <>
                                    <input
                                        type="number"
                                        id={`rating-${order.restaurant.id}`}
                                        name={`rating-${order.restaurant.id}`}
                                        onChange={(event) => handleInputChange(event, order.restaurant.id)}
                                        min="0"
                                        max="5"
                                    />
                                    <button style={{marginLeft: "5px"}} onClick={(event) => rateRestaurant(event, order.restaurant.id)}>
                                        Rate restaurant
                                    </button>
                                    {error && <p style={{ color: 'red' }}>{error}</p>}
                                </>
                            )}
                            
                                                                                     
                        </div>))
                )
                :
                (
                    <p style={{ color: 'red' }}>There are no orders to display!</p>
                )


            )
        }
        </>
    );

}
export default Customer;