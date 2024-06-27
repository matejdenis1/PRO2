import axios from "axios";
import { useLocation, useNavigate, Outlet, Link} from "react-router-dom";
import { useState, useEffect } from "react";
import classes from "./Payment.module.css";
import PaymentMethod from "./PaymentMethod"
function Payment() {


    const [loading, setLoading] = useState(true);
    const [order, setOrder] = useState(null);
    
    const navigate = useNavigate();
    const location = useLocation();
    
    const {customer, restaurant, menu, price, orderId} = location.state;

    const goToPaymentMethod = () => {
        navigate('paymentMethod', {
            state: { customer, restaurant, menu, price, orderId }
        });
    };
    async function payLater(event){
        try{
            event.preventDefault();
        await axios.post("http://localhost:8080/api/v1/payments/create", {
            customer: customer,
            order: order,
            totalAmount: price,
            paymentMethod: "pending",
            paymentStatus: "pending",
            paymentDate: Date.now(),
        }).then((res) => {
            console.log("Payment created!")
            navigate("/")
        })
        }
        catch(err)
        {
            console.log(err)
        }
        
    }

    useEffect(() => {
        axios.get("http://localhost:8080/api/v1/orders/" + orderId).then(response => {
        setOrder(response.data)});
        setLoading(false);

    }, []);

    return (
        <>
        {
            loading ? (console.log("loading")) : (
            <div className={classes.payment}>
                <Outlet />
                <h2>Payment Details</h2>
                <div className={classes.detail}>
                    <h3>Customer Info</h3>
                    <p>Email: {customer.email}</p>
                    <p>Address: {customer.address}</p>
                </div>
                <div className={classes.detail}>
                    <h3>Restaurant Info</h3>
                    <p>Name: {restaurant.name}</p>
                    <p>Address: {restaurant.address}</p>
                </div>
                <div className={classes.detail}>
                    <h3>Menu</h3>
                    {menu.map(item => (
                        <p key={item.name}>{item.name} - {item.amount} pcs</p>
                    ))}
                </div>
                <div className={classes.detail}>
                    <h3>Total Price</h3>
                    <p>{price-2.5} € + delivery fee: 2.5 €</p>
                    <p style={{fontSize: '20pt'}}>{price} €</p>
                </div>
                <div className={classes.buttonContainer}>
                <button onClick={payLater} className={classes.btnPrimary}>Pay Later</button>
                <button onClick={goToPaymentMethod} className={classes.btnPrimary}>Pay Now</button>
                </div>
            </div>
            )
        }
        </>
        
    );
}

export default Payment;