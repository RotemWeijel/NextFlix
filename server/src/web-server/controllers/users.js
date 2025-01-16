const UserService = require('../services/user');
const { hashPassword } = require('../services/password');

/**
 * Controller for handling user-related HTTP requests
 */
exports.register = async (req, res) => {
  try {
    // Extract user data from request body
    const { username, password, full_name, picture } = req.body;
    const hashedPassword = hashPassword(password);

    const userData = { username, password: hashedPassword, full_name, picture };
    await UserService.createUser(userData);

    res.status(201).end();

  } catch (error) {
    res.status(400).json({ error: error.message });
  }
};

exports.getUser = async (req, res) => {
  try {
    const user = await UserService.getUserById(req.params.id);
    res.status(200).json(user);
  } catch (error) {
    if (error.message === 'User not found') {
      res.status(404).json({ error: error.message });
    } else {
      res.status(500).json({ error: error.message });
    }
  }
};