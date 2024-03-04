const express = require('express');
const router = express.Router();

const discountController = require('../controllers/discount.controller');

router.get('/', discountController.getDiscount);
router.post('/create', discountController.createDiscount);
router.put('/update/:id', discountController.updateDiscount);
router.delete('/delete/:id', discountController.deleteDiscount);

module.exports = router;