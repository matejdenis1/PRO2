import React from 'react';
import { useState, useEffect } from 'react';
import classes from "./PaymentMethod.module.css"
import axios from 'axios';
import InputMask from 'react-input-mask';
import {useLocation, useNavigate} from "react-router-dom"
function PaymentMethod(){
    const location = useLocation()
    const navigate = useNavigate()
    const [order, setOrder] = useState(null)
    const [data,setData] = useState(null)
    const {customer, restaurant, menu, price, orderId} = location.state;
    useEffect(() => {
        axios.get("http://localhost:8080/api/v1/orders/" + orderId).then(response => {
        setOrder(response.data);
        setLoading(false);
    })}, []);

    const [loading, setLoading] = useState(true)
    const [selectedOption, setSelectedOption] = useState('');
    const [formData, setFormData] = useState({
        cardNumber: '',
        cardDay: '',
        cardYear: '',
        cvc: ''
      });
    

    async function createPayment(event){
        try{
            event.preventDefault();
        await axios.post("http://localhost:8080/api/v1/payments/create", {
            customer: customer,
            order: order,
            totalAmount: price,
            paymentMethod: selectedOption,
            paymentStatus: "paid",
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

    const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
    };
    const handleOptionChange = (event) => {
        setSelectedOption(event.target.value);
    };

   

    return(
        <>
        {
            loading ? (console.log("Loading...")) :
            (
            <div className={classes.container}>
                <form onSubmit={createPayment}>
                <label style={{ margin: "5px" }}>Select payment method</label>
                <select value={selectedOption} onChange={handleOptionChange}>
                    <option value="blank"></option>
                    <option value="card">Card</option>
                    <option value="paypall">PayPal</option>
                </select>

                {selectedOption === "card" && (
                    <div className={classes.form}>
                        <InputMask
                            mask="9999-9999-9999-9999"
                            maskChar="0"
                            value={formData.cardNumber}
                            onChange={handleInputChange}
                        >
                            {(inputProps) => (
                                <input
                                    {...inputProps}
                                    type="text"
                                    name="cardNumber"
                                    id="card-number"
                                    placeholder="Card Number"
                                    className={classes.input}
                                />
                            )}
                        </InputMask>
                        <div className={classes.inputGroup}>
                            <input
                                type="number"
                                name='cardMonth'
                                id='card-month'
                                placeholder='Month'
                                min="1"
                                max="12"
                                value={formData.cardMonth}
                                onChange={handleInputChange}
                                className={classes.smallInput}
                            />
                            <input
                                type="number"
                                name='cardYear'
                                id='card-year'
                                placeholder='Year'
                                min="20"
                                max="30"
                                value={formData.cardYear}
                                onChange={handleInputChange}
                                className={classes.smallInput}
                            />
                            <input
                                type="text"
                                name='cvc'
                                id='cvc'
                                placeholder='CVC'
                                maxLength="3"
                                value={formData.cvc}
                                onChange={handleInputChange}
                                className={classes.smallInput}
                            />
                        </div>
                        <button type='submit'>Confirm Payment</button>
                    </div>
                )}

                {selectedOption === "paypall" && (
                    <div className={classes.form}>
                        <button type='submit'>Connect to PayPal</button>
                    </div>
                )}
            </form>
            </div>)
        }
        
        </>
    );
}
export default PaymentMethod;