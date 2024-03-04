const express = require("express");
const router = express.Router();

const loginController = require("../controllers/auth.controller")

router.post('/loginStaff', loginController.loginStaff)
router.post('/loginCustomer', loginController.loginCustomer)
router.post('/register', loginController.register)


module.exports = router;