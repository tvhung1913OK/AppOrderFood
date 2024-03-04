const mongoose = require('mongoose');

const CustomerSchema = new mongoose.Schema({
    idUser: { type: mongoose.Schema.Types.ObjectId, ref: 'user', required: true },
});

const CustomerModel = mongoose.model('customer', CustomerSchema);

module.exports = CustomerModel;