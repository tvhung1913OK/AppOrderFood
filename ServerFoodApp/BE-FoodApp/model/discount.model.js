const mongoose = require('mongoose');
const moment = require('moment');

const discount = new mongoose.Schema({
    name: { type: String, required: true },
    description: { type: String, required: true },
    discountPerson: { type: Number, required: true },
    dateStart: { type: String, default: moment(new Date()).format('L')},
    dateEnd: { type: String, required: true},
});

module.exports = mongoose.model('discount', discount);