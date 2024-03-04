const express = require('express');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');

const categoryRoute = require('./router/category.router');
const productRouter = require('./router/product.router');
const staffRouter = require('./router/staff.router');
const customerRouter = require('./router/customer.router');
const orderItemRouter = require('./router/orderitem.router');
const discountRouter = require('./router/discount.router');
const orderDetailRouter = require('./router/orderDetail.router');
const authRouter = require('./router/auth.router');
const changePasswordRouter = require('./router/password.router')
const app = express();

const uri = 'mongodb+srv://phungchikien196:Qa4168ciXnRnjV9G@apppolylib.5gjczzc.mongodb.net/FoodApp';
try {
      mongoose.connect(uri);
      console.log('Connect successfully!');
} catch (error) {
      console.log('Connect error!!!');
}

const port = 3000;

app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(express.static('public'));
app.use('/image', express.static('image'));
app.use(bodyParser.json());

app.use('/api/category', categoryRoute);
app.use('/api/product', productRouter);
app.use('/api/staff', staffRouter)
app.use('/api/customer', customerRouter)
app.use('/api/order-item', orderItemRouter);
app.use('/api/discount', discountRouter);
app.use('/api/order-detail', orderDetailRouter);
app.use('/api/auth', authRouter);
app.use('/api/change-password', changePasswordRouter);

app.get('/', (req, res) => {
      res.redirect('/api');
});

app.listen(port, () => {
      console.log(`Example app listening on port ${port}`);
});
module.exports = app;
