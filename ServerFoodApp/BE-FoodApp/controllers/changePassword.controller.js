const mdUser = require("../model/user.model");

const { initializeApp } = require("firebase/app");
const {
  getAuth,
  reauthenticateWithCredential,
  updatePassword,
  EmailAuthProvider,
} = require("firebase/auth");
const firebaseConfig = require("../config/firebase.config");

exports.changePassword = async (req, res) => {
  // Initialize Firebase
  try {
    const app = initializeApp(firebaseConfig);
    const auth = getAuth(app);
    const currentUser = auth.currentUser;

    const { _id, oldPassword, newPassword, reNewPassword } = req.body;
    const userDocument = await mdUser.findById({ _id: _id });
    if (currentUser) {
      // const credential = EmailAuthProvider.credential(
      //   currentUser.email,
      //   oldPassword
      // );
      // if (!credential) {
      //   return res.status(400).send("Old password is incorrect.");
      // }
      if (userDocument.password != oldPassword) {
        return res.status(400).send("Old password is incorrect.");
      }
      if (reNewPassword != newPassword) {
        return res.status(400).send("RePassword is incorrect.");
      }
      // reauthenticateWithCredential(currentUser, credential).then(() => {
        updatePassword(currentUser, newPassword).then(async () => {
          // Update password in MongoDB

          userDocument.password = newPassword;
          await userDocument.save();

          res.status(200).send(userDocument);
        });
      // });
    } else {
      console.error("User is not logged in.");
    }
  } catch (error) {
    console.log(error.message);
  }
};
