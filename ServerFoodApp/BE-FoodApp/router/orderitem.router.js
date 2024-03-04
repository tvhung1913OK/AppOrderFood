const express = require('express');
const router = express.Router();
const orderItemController = require('../controllers/orderitem.controllers');


router.get('/', orderItemController.getAllOrderItems);
router.get('/getByIdCustomer/:idCustomer', orderItemController.getOrderItemsByCustomerId);
router.post('/addOrderItem', orderItemController.createOrderItem);
router.put('/updateOrderItem/:id', orderItemController.updateOrderItem);
router.delete('/deleteOrderItem/:id', orderItemController.deleteOrderItem);

module.exports = router;
