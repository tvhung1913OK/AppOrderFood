const express = require("express");
const router = express.Router();

const orderDetailController = require("../controllers/orderDetail.controller")

router.get('/', orderDetailController.getData);
router.get('/:customerId', orderDetailController.getDataByIdCustomer);
router.get('/getDailyRevenue/:date', orderDetailController.getDailyRevenueData);
router.post('/create', orderDetailController.createOrderDetail);
router.put('/update/:id', orderDetailController.updateOrderDetail);
router.delete('/delete/:id', orderDetailController.deleteOrderDetail);

module.exports = router;