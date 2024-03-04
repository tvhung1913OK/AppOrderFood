const express = require("express");
const router = express.Router();

const customerController = require("../controllers/customer.controller")

router.get('/', customerController.getData)
// router.post('/addCustomer', customerController.addData)
router.put('/updateCustomer/:_id', customerController.updateData)
router.delete('/deleteCustomer/:_id', customerController.deleteData)

router.post('/searchPhoneCustomer', customerController.search)

module.exports = router;