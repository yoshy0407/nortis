
export interface ValidationResult {
    error: boolean;
    message?: string;
}

export function success(): ValidationResult {
    return {
        error: false
    }
}

export function failure(message: string): ValidationResult {
    return {
        error: true,
        message
    }
}

function validation(value: string, message: string, test: (v: string) => boolean) {
    if (test(value)) {
        return success()
    } else {
        return failure(message);
    }
}

export function notNull(value: string, message: string): ValidationResult {
    return validation(value, message, v => v !== null || v !== undefined);
}

export function notEmpty(value: string, message: string): ValidationResult {
    return validation(value, message, v => v !== null || v !== undefined || v !== '');
}
