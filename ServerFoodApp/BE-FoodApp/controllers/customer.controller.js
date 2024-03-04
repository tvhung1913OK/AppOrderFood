const mdUser = require('../model/user.model');
const mdCustomer = require('../model/customer.model');
const { initializeApp } = require("firebase/app");
const { getAuth, createUserWithEmailAndPassword, deleteUser } = require("firebase/auth");
const firebaseConfig = require("../config/firebase.config");

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app); // Khởi tạo Firebase Authentication SDK


exports.getData = async (req, res) => {
    let listCustomer = await mdCustomer.find().populate('idUser');

    if (listCustomer.length > 0) {
        return res.status(200).json({ listCustomer, check: 'có dữ liệu' });
    }
    else {
        return res.status(404).json({ check: 'không có dữ liệu' });
    }
}

// exports.addData = async (req, res) => {
//     try {
//         const { name, phone, password, date, sex, image, email, address } = req.body;
//         const phoneNumberRegex = /^[0-9]{10}$/;
//         const emailRegex = /^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]{2,}$/;

//         if (!name || !phone || !password || !date || !sex || !image || !email || !address) {
//             return res.status(400).json({ status: 0, message: 'Dữ liệu không hợp lệ' });
//         } else if (isNaN(phone)) {
//             return res.status(400).json({ status: 0, message: 'Số điện thoại phải là số.' });
//         } else if (!phoneNumberRegex.test(phone)) {
//             return res.status(400).json({ status: 0, message: 'Số điện thoại sai định dạng.' });
//         } else if (!emailRegex.test(email)) {
//             return res.status(400).json({ status: 0, message: 'Email sai định dạng.' });
//         }

//         const validatePhone = await mdUser.findOne({ phone });
//         if (validatePhone) {
//             return res.status(400).json({ status: 1, message: 'Mỗi số điện thoại chỉ được đăng ký 1 lần' })
//         }
//         const validateEmail = await mdUser.findOne({ email });
//         if (validateEmail) {
//             return res.status(400).json({ status: 1, message: 'Mỗi email chỉ được đăng ký 1 lần' })
//         }

//         // Tạo một người dùng với thông tin chung và loại tài khoản (accType) là Customer
//         const newUser = new mdUser({
//             name,
//             phone,
//             password,
//             date,
//             sex,
//             image,
//             email,
//             address,
//             accType: 'Customer',
//         });
//         // Tạo một nhân viên với vai trò là admin
//         const newCustomer = new mdCustomer({
//             idUser: newUser._id
//         });
//         await newCustomer.save();

//         // Gán ID người dùng cho Customer

//         const result = await newUser.save();

//         res.status(201).json({ message: 'Customer created successfully', data: result });
//     } catch (error) {
//         console.error(error);
//         res.status(500).json({ error: 'Lỗi máy chủ nội bộ' });
//     }
// };

exports.updateData = async (req, res) => {
    try {
        const { _id } = req.params;
        const { name, phone, date, sex, image, address } = req.body;
        const phoneNumberRegex = /^[0-9]{10}$/;
        const emailRegex = /^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]{2,}$/;


        if (!name || !phone || !date || !sex || !image || !address) {
            return res.status(400).json({ status: 0, message: 'Dữ liệu không hợp lệ.' });
        } else if (isNaN(phone)) {
            return res.status(400).json({ status: 0, message: 'Số điện thoại phải là số.' });
        } else if (!phoneNumberRegex.test(phone)) {
            return res.status(400).json({ status: 0, message: 'Số điện thoại sai định dạng.' });
        }

        const userUpdateData = {
            name,
            phone,
            date,
            sex,
            image,
            email,
            address,
        };

        const userUpdateResult = await mdUser.findOneAndUpdate({ _id: _id }, userUpdateData, { new: true });

        if (!userUpdateResult) {
            return res.status(404).json({ status: 0, message: 'Không tìm thấy người dùng' });
        }

        res.status(200).json({ message: 'Cập nhật thông tin người dùng thành công', data: userUpdateResult });
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ' });
    }
}

exports.deleteData = async (req, res) => {
    try {
        const { _id } = req.params;

        const resultUser = await mdUser.findByIdAndDelete(_id);
        if (!resultUser) {
            return res.status(404).json({ error: "Danh mục không tồn tại" });
        }
        const resultCustomer = await mdCustomer.findOneAndDelete({ idUser: _id });
        if (!resultCustomer) {
            return res.status(404).json({ error: "Danh mục không tồn tại" });
        }

        // Xóa tài khoản người dùng từ Firebase Authentication
        const user = auth.currentUser;
        if (user) {
            deleteUser(user)
                .then(() => {
                    console.log("Xóa tài khoản Firebase thành công");
                })
                .catch((error) => {
                    console.error("Lỗi khi xóa tài khoản Firebase:", error);
                });
        }

        res.status(204).json({ message: 'Xóa thông tin người dùng và nhân viên thành công', data: { user: resultUser, Customer: resultCustomer } });
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ' });
    }
}

exports.search = async (req, res) => {
    try {
        const { phone } = req.body;

        if (!phone) {
            return res.status(400).json({ status: 0, message: 'Vui lòng cung cấp số điện thoại.' });
        }

        // Sử dụng biểu thức chính qui để kiểm tra xem số điện thoại chỉ chứa số
        const phoneNumberRegex = /^[0-9]+$/;

        if (!phoneNumberRegex.test(phone)) {
            return res.status(400).json({ status: 0, message: 'Số điện thoại chỉ được chứa số.' });
        }

        // Sử dụng biểu thức chính qui ($regex) để tìm kiếm mờ số điện thoại từ bảng User
        const userResults = await mdUser.find({ phone: { $regex: phone, $options: 'i' } });
        const listCustomer = await mdCustomer.find();

        if (!userResults || userResults.length === 0) {
            return res.status(404).json({ status: 0, message: 'Không tìm thấy người dùng với số điện thoại này.' });
        }

        let listUser = userResults.filter(user => user.accType === "Customer");

        const dataJson = listUser.map(user => {
            const dataCustomer = listCustomer.filter(Customer => Customer.idUser.equals(user._id));
            return {
                data: {

                    name: user.name,
                    phone: user.phone,
                    password: user.password,
                    date: user.date,
                    sex: user.sex,
                    image: user.image,
                    email: user.email,
                    address: user.address,
                    accType: user.accType,
                    Customer: dataCustomer
                }
            }
        })

        if (dataJson.length > 0) {
            res.status(200).json({ status: 1, message: 'Tìm thấy người dùng và nhân viên.', data: dataJson });
        } else {
            res.status(404).json({ status: 0, message: 'Không tìm thấy kết quả phù hợp.' });
        }
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Lỗi máy chủ nội bộ' });
    }
}