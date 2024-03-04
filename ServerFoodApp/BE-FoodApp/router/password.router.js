const express = require('express');
const router = express.Router();
const changePassword = require('../controllers/changePassword.controller')


router.post('/', changePassword.changePassword);

module.exports = router;
