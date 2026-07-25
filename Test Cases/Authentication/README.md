# Login Test Cases - Authentication Module

## Overview
Complete set of Login test cases covering positive and negative scenarios.

## Test Cases Created

### TC_Login_001_Valid_Credentials
**Type**: Positive Test, Smoke Test
**Description**: Test successful login with valid email and password
**Expected Result**: 
- User logs in successfully
- Redirects to /home
- Home page elements visible

### TC_Login_002_Invalid_Email_Format
**Type**: Negative Test, Validation Test
**Description**: Test login with invalid email format (no @ symbol)
**Expected Result**:
- Form validation prevents submission
- User remains on login page
- Error displayed (client-side or toast)

### TC_Login_003_Invalid_Credentials
**Type**: Negative Test, Security Test
**Description**: Test login with valid email but wrong password
**Expected Result**:
- Authentication fails
- User remains on login page
- Error message displayed
- Login form still accessible

### TC_Login_004_Empty_Fields
**Type**: Negative Test, Validation Test
**Description**: Test form submission with empty email and password
**Expected Result**:
- Required field validation prevents submission
- User remains on login page
- Form still visible

### TC_Login_005_Short_Password
**Type**: Negative Test, Validation Test
**Description**: Test login with password less than 8 characters
**Expected Result**:
- Password length validation prevents submission
- User remains on login page

## Test Data Requirements

### Valid Test Account
- Email: testuser@example.com
- Password: Password123!
- Account Status: Active, Not Locked

### Object Repository Dependencies
- Auth/Login/form_login
- Auth/Login/input_email
- Auth/Login/input_password
- Auth/Login/btn_login
- Auth/Login/link_register
- Common/badge_home_account_number

## Execution Notes
1. Run tests against https://gopek.live/login
2. Ensure test account exists and is not locked
3. Each test is independent - can run in any order
4. Tests include screenshots for evidence
5. All tests use proper waits for page load

## Coverage
- ✓ Positive login flow
- ✓ Email format validation
- ✓ Password validation
- ✓ Empty field validation
- ✓ Authentication failure handling

## Test Status
**Status**: ✅ Ready for Execution
**Files**: 5 Test Case files (.tc)
**Format**: Valid Katalon XML
**Total Lines**: ~400 lines across 5 files
