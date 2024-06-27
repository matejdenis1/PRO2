import axios from 'axios'
import {useState, useEffect} from 'react'

import styles from './Order.module.css'
function Order({id}) {

    const [loading, setLoading] = useState(true);
    const [orders, setOrders] = useState(null);
    const [payments, setPayments] = useState(null);

    const [refresh, setRefresh] = useState(false);

    const deliverOrder = (orderId) => {
        axios.post('http://localhost:8080/api/v1/orders/deliver/' + orderId).then(res => {
            if(res){
                setRefresh(prev => !prev)
                console.log("Order was delivered")
            }
            else{
                console.log("There was an error!")
            }
            
        }).catch(err => {
            console.error(err);
        })
    }
    const updatePayment = (orderId) => {

        const paymentDetails = orderPayments[orderId];
        if (paymentDetails) {
            deliverOrder(orderId)
            axios.post('http://localhost:8080/api/v1/payments/updatePayments', {
            id: orderId,
            paymentMethod: paymentDetails.selectedPaymentMethod,
            paymentStatus: paymentDetails.selectedPaymentStatus,
            paymentDate: Date.now()
          })
          .then(response => {
            setRefresh(prev => !prev)
            console.log(`Payment for order ${orderId} updated successfully:`, response.data);
          })
          .catch(error => {
            console.error(`Failed to update payment for order ${orderId}:`, error);
          });
        }
      };



    const [orderPayments, setOrderPayments] = useState({});

    const handlePaymentStatusChange = (orderId, event) => {
        const { value } = event.target;
        setOrderPayments(prevState => ({
            ...prevState,
            [orderId]: {
                ...prevState[orderId],
                selectedPaymentStatus: value
            }
        }));
    };

    // Function to handle changes in payment method
    const handlePaymentMethodChange = (orderId, event) => {
        const { value } = event.target;
        setOrderPayments(prevState => ({
            ...prevState,
            [orderId]: {
                ...prevState[orderId],
                selectedPaymentMethod: value
            }
        }));
    };
    useEffect(() => {
        axios.get(`http://localhost:8080/api/v1/orders/courierOrders/${id}`)
                .then(response => {
                    const ordersData = response.data;
                    setOrders(ordersData);

                    // Create an array of promises for fetching payments
                    const paymentPromises = ordersData.map(order => 
                        axios.get(`http://localhost:8080/api/v1/payments/` + order.id)
                            .then(res => (
                                    { 
                                        orderId: order.id,
                                        paymentData: res.data 
                                    }
                            ))
                    );

                    // Resolve all payment promises
                    return Promise.all(paymentPromises);
                })
                .then(paymentResults => {
                    // Create a mapping of orderId to paymentData
                    const paymentsData = paymentResults.reduce((acc, payment) => {
                        acc[payment.orderId] = payment.paymentData;
                        return acc;
                    }, {});
                    setPayments(paymentsData);
                    setLoading(false);
                })
                .catch(error => {
                    console.error("There was an error!", error);
                    setError(error);
                    setLoading(false);
                });
                
        }, [refresh]);

        return (
            <>
        {
            loading ? (
                console.log("Loading")) :
            (
                
                <div className={styles.container}>
            {
            orders.map(order => (
                <div key={order.id} className={styles.orderPaymentContainer}>
                    <div className={styles.orderDetails}>
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
                            {
                                !order.delivered ? (
                                    <button onClick={() => (deliverOrder(order.id))}>Deliver Order</button>
                                )
                                :
                                (
                                    <p style={{color: "green"}}>Order was delivered</p>
                                )
                            }                                                              
                            
                        </div>
                    <div className={styles.paymentDetails}>
                        <p><strong>Payment Details:</strong></p>
                        {
                            payments[order.id] ? (
                                <>
                                    <form>
                                            <div>
                                                <label>Payment Status:</label>
                                                <select
                                                    value={orderPayments[order.id]?.selectedPaymentStatus || ''}
                                                    onChange={(e) => handlePaymentStatusChange(order.id, e)}
                                                >
                                                    
                                                    <option value="paid">Paid</option>
                                                    <option value="returned">Returned</option>
                                                </select>
                                            </div>
                                            <div>
                                                <label>Payment Method:</label>
                                                <select
                                                    value={orderPayments[order.id]?.selectedPaymentMethod || ''}
                                                    onChange={(e) => handlePaymentMethodChange(order.id, e)}
                                                >
                                                    <option value="card">Card</option>
                                                    <option value="cash">Cash</option>
                                                    <option value="canceled"></option>
                                                </select>
                                            </div>
                                            <p>Payment Amount: {payments[order.id].totalAmount}</p>
                                            <button type="button" onClick={() => updatePayment(order.id)}>Update Payment</button>
                                        </form>
                                                              
                                    
                                </>
                            ) : (
                                <p style={{color: "green"}}>Payment was already proccessed!</p>
                            )
                        }
                    </div>
                </div>
            ))}
            </div>   
            )
        }
        </>
        );
}

export default Order;