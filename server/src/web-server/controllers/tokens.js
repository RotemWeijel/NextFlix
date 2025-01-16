// Token Controller - manages user authentication
const User = require('../models/user');
const Token = require('../models/token');
const { hashPassword } = require('../services/password');
const { generateToken } = require('../services/token');

exports.login = async (req, res) => {
  try {
    const { username, password } = req.body;
    const hashedPassword = hashPassword(password); // Hash the input password

    const user = await User.findOne({ username, password: hashedPassword });
    if (!user) {
      return res.status(401).json({ error: 'Invalid username or password' }); // Authentication failed
    }

    const token = new Token({ userId: user._id, token: generateToken() }); // Generate a new token
    await token.save();

    res.status(200).json({ userId: user._id, token: token.token }); // Return the token
  } catch (error) {
    res.status(500).json({ error: error.message }); // Handle unexpected errors
  }
};
