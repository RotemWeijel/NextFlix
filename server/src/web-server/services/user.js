const User = require('../models/user');

/**
 * Service class for handling user-related business logic
 */
class UserService {
  /**
   * Validates if a string is a valid URL
   * @param {string} string - URL string to validate
   * @returns {boolean} - True if valid URL, false otherwise
   */
  isValidUrl(string) {
    try {
      new URL(string);
      return true;
    } catch (_) {
      return false;
    }
  }

  /**
   * Validates user data before creation
   * @param {Object} userData - User data to validate
   * @returns {Array} - Array of validation error messages
   */
  validateUserData(userData) {
    const errors = [];
    
    if (!userData.username) errors.push('Username is required');
    if (!userData.password) errors.push('Password is required');
    if (userData.picture && !this.isValidUrl(userData.picture)) {
      errors.push('Invalid image URL format');
    }

    return errors;
  }

  /**
   * Creates a new user in the database
   * @param {Object} userData - User data for creation
   * @returns {Promise<User>} - Created user object
   */
  async createUser(userData) {
    const validationErrors = this.validateUserData(userData);
    if (validationErrors.length > 0) {
      throw new Error(validationErrors.join(', '));
    }

    const newUser = new User({
      username: userData.username,
      password: userData.password,
      name: userData.name,
      picture: userData.picture
    });

    return await newUser.save();
  }

  /**
   * Retrieves a user by ID
   * @param {string} id - User ID
   * @returns {Promise<User>} - User object (without password)
   */
  async getUserById(id) {
    const user = await User.findById(id).select('-password');
    if (!user) throw new Error('User not found');
    return user;
  }
}

module.exports = new UserService();