# Validation Object Coverage for Negative Testing

## ✓ AVAILABLE FOR NEGATIVE TEST VALIDATION (66 Total Objects)

### Error/Validation Cards (2 objects)
- ✓ `card-account-valid` - Green success card when recipient found
- ✓ `card-account-invalid` - Red error card when recipient not found/invalid

### Form Navigation (4 additional objects)
- ✓ `btn-continue-transfer` - Transfer recipient form continue
- ✓ `btn-continue-to-pin` - Generic PIN step navigation
- ✓ `btn-back-withdraw` - Withdraw form back button
- ✓ `btn-continue-withdraw` - Withdraw form continue

### Existing Validation-Relevant Objects
- ✓ All input fields (can trigger validation by entering invalid data)
- ✓ All submit buttons (can verify enabled/disabled states)
- ✓ Quick amount buttons (boundary value testing)
- ✓ PIN keypad (complete 0-9 + backspace)

## ❌ NOT AVAILABLE (No data-testid in Frontend Code)

### Toast Notifications
- ❌ Success toast messages
- ❌ Error toast messages  
- ❌ Toast container elements
**Why**: Sonner library doesn't add data-testid by default

### Transaction Status Page
- ❌ Success status icon/text
- ❌ Failed status icon/text
- ❌ Pending status icon/text
**Why**: StatusDetailCard component has no data-testid attributes

### Form Field Validation Errors
- ❌ Individual field error messages (FieldError component)
**Why**: Uses `role="alert"` but no data-testid
**Note**: Can validate via form submission failure, but cannot assert exact error text

## NEGATIVE TEST STRATEGY WITH CURRENT OBJECTS

### What You CAN Test:
1. **Input Validation**
   - Enter invalid data → Submit → Verify button stays disabled
   - Enter invalid email format → Verify form doesn't submit
   - Enter insufficient balance amount → Verify validation

2. **Recipient Validation** (Transfer)
   - Enter invalid account → Verify `card-account-invalid` appears
   - Enter valid account → Verify `card-account-valid` appears

3. **PIN Validation**
   - Enter wrong PIN → Verify submit fails (indirect)
   - Account locking after 3 attempts (check via login failure)

4. **Button State Validation**
   - Verify submit buttons disabled when form invalid
   - Verify continue buttons appear after valid input

### What You CANNOT Test:
1. **Toast Message Content** - Cannot assert exact error messages from API
2. **Status Page Success/Failure Text** - Cannot verify "Transfer Successful" text
3. **Field-Level Error Messages** - Cannot assert "Email format invalid" under input

## RECOMMENDATION FOR COMPREHENSIVE NEGATIVE TESTING

If you need to validate error messages and status text, frontend developer must add:

```tsx
// In StatusDetailCard.tsx
<span 
  className="text-2xl font-bold"
  data-testid="status-message"  // ADD THIS
>
  {isSuccess ? `${typeLabel} Successful` : ...}
</span>

// In sonner configuration (layout.tsx or Toaster component)
<Sonner 
  toastOptions={{
    classNames: {
      toast: "toast-notification",  // ADD data-testid via class
    }
  }}
/>

// In FieldError component (field.tsx)
<div
  role="alert"
  data-testid="field-error"  // ADD THIS
  className={cn("text-sm font-normal text-destructive", className)}
>
```

## CURRENT TEST COVERAGE: ~70%

**Can Test**:
- ✓ Input field validation (form level)
- ✓ Recipient validation (visual card feedback)
- ✓ Button state changes
- ✓ Navigation flow errors
- ✓ PIN validation (indirect via form failure)

**Cannot Test**:
- ❌ Exact error message content from API
- ❌ Toast notification appearance/content
- ❌ Status page success/failure indicators
- ❌ Individual field error text validation
