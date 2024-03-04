const mongoose = require('mongoose');

const UserSchema = new mongoose.Schema({
    name: { type: String, required: true },
    phone: { type: String, required: true },
    password: { type: String, required: true },
    date: { type: String, required: true },
    sex: { type: String, required: true },
    image: { type: String, required: true },
    email: { type: String, required: true },
    address: { type: String, required: true },
    accType: { type: String, required: true },
});

const UserModel = mongoose.model('user', UserSchema);

module.exports = UserModel;