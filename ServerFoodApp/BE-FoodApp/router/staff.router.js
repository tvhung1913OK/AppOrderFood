const express = require("express");
const router = express.Router();

const staffController = require("../controllers/staff.controller")

router.get('/', staffController.getData)
router.post('/addStaff', staffController.addData)
router.put('/updateStaff/:_id', staffController.updateData)
router.delete('/deleteStaff/:_id', staffController.deleteData)

router.post('/searchPhoneStaff', staffController.search)

module.exports = router;