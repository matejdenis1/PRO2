import './App.css';
import {Routes, Route} from 'react-router-dom';
import Home from './components/Home';
import Detail from './components/Detail';
import Payment from './components/Payment';
import PaymentMethod from './components/PaymentMethod';

function App() {
return (
  <>
    <Routes>
      <Route path='*' element={<Home/>} />
      <Route path='/payment/*' element={<Payment/>}>
        <Route path='paymentMethod' element={<PaymentMethod/>}/>
      </Route>
    </Routes>
  </>
);
}

export default App;
