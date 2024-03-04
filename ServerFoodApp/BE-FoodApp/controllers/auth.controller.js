const mdUser = require("../model/user.model");
const mdStaff = require("../model/staff.model");
const mdCustomer = require("../model/customer.model");

const { initializeApp } = require("firebase/app");
const {
  getAuth,
  signInWithEmailAndPassword,
  createUserWithEmailAndPassword,
} = require("firebase/auth");
const firebaseConfig = require("../config/firebase.config");

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app); // Khởi tạo Firebase Authentication SDK

exports.loginCustomer = async (req, res) => {
  const { email, password } = req.body;
  // Try to sign in the user with the email and password
  try {
    const userCredential = await signInWithEmailAndPassword(
      auth,
      email,
      password
    );
    const user = userCredential.user;

    // Check if the sign in was successful
    if (user) {
      // Get the user data
      const user = await mdUser.findOne({ email });
      const customer = await mdCustomer.findOne({ idUser: user._id });

      // Check the user type
      if (user.accType === "Customer") {
        const data = {
            user: user,
            customer: customer,
        }
        res.status(200).json(data);
      } else {
        res.status(401).json({ error: "Invalid user type" });
      }
    }
  } catch (error) {
    // Return an error
    console.error("Đăng nhập không thành công:", error.message);
    res.status(401).json({ error: "Invalid email or password" });
  }
};

exports.loginStaff = async (req, res) => {
  const { email, password } = req.body;
  // Try to sign in the user with the email and password
  try {
    const userCredential = await signInWithEmailAndPassword(
      auth,
      email,
      password
    );
    const user = userCredential.user;

    // Check if the sign in was successful
    if (user) {
      // Get the user data
      const userStaff = await mdUser.findOne({ email });

      const staff = await mdStaff.findOne({ idUser: userStaff._id });

      // Check the userStaff type
      if (userStaff.accType === "Staff") {
        const data = {
          user: userStaff,
          staff: staff,
        };
        res.status(200).json(data);
      } else {
        res.status(401).json({ error: "Invalid staff type" });
      }
    }
  } catch (error) {
    // Return an error
    console.error("Đăng nhập không thành công:", error.message);
    res.status(401).json({ error: "Invalid email or password" });
  }
};

exports.register = async (req, res) => {
  const { name, phone, password, date, sex, image, email, address } = req.body;

  const phoneNumberRegex = /^[0-9]{10}$/;
  const emailRegex = /^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]{2,}$/;

  if (
    !name ||
    !phone ||
    !password ||
    !date ||
    !sex ||
    !image ||
    !email ||
    !address
  ) {
    return res.status(400).json({ status: 0, message: "Dữ liệu không hợp lệ" });
  } else if (isNaN(phone)) {
    return res
      .status(400)
      .json({ status: 0, message: "Số điện thoại phải là số." });
  } else if (!phoneNumberRegex.test(phone)) {
    return res
      .status(400)
      .json({ status: 0, message: "Số điện thoại sai định dạng." });
  } else if (!emailRegex.test(email)) {
    return res.status(400).json({ status: 0, message: "Email sai định dạng." });
  }

  const validatePhone = await mdUser.findOne({ phone });
  if (validatePhone) {
    return res
      .status(400)
      .json({ status: 1, message: "Mỗi số điện thoại chỉ được đăng ký 1 lần" });
  }
  const validateEmail = await mdUser.findOne({ email });
  if (validateEmail) {
    return res
      .status(400)
      .json({ status: 1, message: "Mỗi email chỉ được đăng ký 1 lần" });
  }

  try {
    const userCredential = await createUserWithEmailAndPassword(
      auth,
      email,
      password
    );
    const user = userCredential.user;

    // Check if the user was created successfully
    if (user) {
      const newUser = new mdUser({
        name,
        phone,
        password,
        date,
        sex,
        image,
        email,
        address,
        accType: "Customer",
      });
      // Tạo một nhân viên với vai trò là admin
      const newCustomer = new mdCustomer({
        idUser: newUser._id,
      });
      await newCustomer.save();

      const result = await newUser.save();

      res.status(200).json(user);
    }
  } catch (error) {
    // Return an error
    console.error("Đăng ký không thành công:", error.message);
    res.status(400).json({ error: "Failed to create user" });
  }
};
