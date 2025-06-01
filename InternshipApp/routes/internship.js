const express = require('express');
const router = express.Router();
const Internship = require('../models/Internship');
const jwt = require('jsonwebtoken');

const JWT_SECRET = process.env.JWT_SECRET;

const authenticateToken = (req, res, next) => {
  const authHeader = req.headers['authorization'];
  const token = authHeader && authHeader.split(' ')[1]; // Extract token after "Bearer"

  if (!token) return res.status(401).json({ msg: 'No token provided' });

  jwt.verify(token, JWT_SECRET, (err, user) => {
    if (err) return res.status(403).json({ msg: 'Token is not valid' });

    req.user = user; // Save decoded user info in req.user
    next();
  });
};

// Add internship (admin only)
router.post('/', authenticateToken, async (req, res) => {
  try {
    const internship = new Internship(req.body);
    const savedInternship = await internship.save();
    res.status(201).json(savedInternship);
  } catch (err) {
    res.status(400).json({ msg: 'Error creating internship' });
  }
});

// Get internships (public)
router.get('/', async (req, res) => {
  try {
    const internships = await Internship.find().sort({ postedAt: -1 });
    res.status(200).json(internships);
  } catch (err) {
    res.status(500).json({ msg: 'Server error' });
  }
});

module.exports = router;
