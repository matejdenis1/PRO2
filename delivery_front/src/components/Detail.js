import React from 'react'
import axios from 'axios';
import { Route, Routes, Link, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import classes from './Detail.module.css'



const Detail = ({id, profile}) => {
        const [loading, setLoading] = useState(true);
        const [data, setData] = useState(null);
        const [checkedItems, setCheckedItems] = useState({});
        const [amounts, setAmounts] = useState({});
        const [error, setError] = useState('');

        const navigate = useNavigate();
        
        const handleInputChange = (event) => {
                const { name, checked } = event.target;
                setCheckedItems({ ...checkedItems, [name]: checked});
            };
            const handleAmountChange = (event) => {
                const { name, value } = event.target;
                setAmounts({ ...amounts, [name]: value });
            };

        useEffect(() => {
            axios.get("http://localhost:8080/api/v1/restaurants/" + id).then(response => {
            setData(response.data);
            setLoading(false);
            });
        }, []);

        async function order(event){
            event.preventDefault();
            const menu = Object.keys(checkedItems)
                    .filter(key => checkedItems[key])
                    .map(key => {
                    const menuItem = data.menu.find(item => item.name === key); // Find the corresponding menu item
                    return { name: menuItem.name, price: menuItem.price, amount: amounts[key] || 1 }; // Construct the object
                    });

            let price = 0;
            menu.map(item => {
                price += (item.amount * item.price);
            })
            const deliveryFee = 2.5;
            try{
                if(profile == null){
                    setError("You must be logged in")
                    return;
                }
                if(menu.length == 0){
                    setError("You must check atleast one item to order")
                    return
                }
                await axios.post("http://localhost:8080/api/v1/orders/create", {
                    customer: profile,
                    restaurant: data,
                    menu: menu,
                    price: price+deliveryFee
                }).then((res) => {
                    if(res.data != null){
                        console.log("Order created!"),
                        navigate('/payment', {
                            state: {
                                orderId: res.data,
                                customer: profile,
                                restaurant: data,
                                menu: menu,
                                price: price+deliveryFee,
                            }
                        });
                    }
                    else{
                        console.log("Order not created!")
                    }
                }
            )}
            catch(err){
                alert(err);
            }
        }

    return (
        <>
            {
                loading ? (
                    console.log("Loading..")
                  ) : (
                        <div className={classes.restaurant}>
                            <img src={data.picture} alt='picture' width="200" height="200"/>
                            <p>Name: {data.name}</p>
                            <p>Rating: {data.rating}</p>
                            <p>Address: {data.address}</p>
                            <form onSubmit={order}>
                            {
                                data.menu.map((item) => (
                                    <div>

                                        <input type="checkbox" id={item.name} name={item.name} checked={checkedItems[item.name] || false} onChange={handleInputChange}/>
                                        <input
                                            type="number"
                                            id={`amount-${item.name}`}
                                            name={item.name}
                                            value={amounts[item.name] || 0}
                                            onChange={handleAmountChange}
                                            min="0"
                                        />
                                        <label htmlFor={item.name}>{item.name}</label> 
                                        <label htmlFor={item.price}>{item.price} €</label>                                      
                                    </div>                                  
                                )                                                                                      
                                )
                            }                    
                            <button type='submit' className={classes.btnPrimary}>ORDER</button>
                            
                            </form>
                            {error && <p style={{ color: 'red' }}>{error}</p>}

                        </div>
                )
                
            }
            
           
        </>
    )
}
export default Detail