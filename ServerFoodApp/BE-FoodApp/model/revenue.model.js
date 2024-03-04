const mongoose = require('mongoose');

const RevenueSchema = new mongoose.Schema({
    totalRevenue: { type: Number, required: true },
    date: { type: Date, default: Date.now },
});

const RevenueModel = mongoose.model('revenue', RevenueSchema);

module.exports = RevenueModel;
