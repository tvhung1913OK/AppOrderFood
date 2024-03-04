const express = require('express');
const revenueController = require('../controllers/revenue.controller');

const router = express.Router();



router.get('/daily-for-staff/:staffId', revenueController.getDailyRevenueForStaff);


module.exports = router;
