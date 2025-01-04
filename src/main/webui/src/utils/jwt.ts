export const isExpired = (expires_at: number): boolean => {
    const expirationDate = new Date(expires_at * 1000);
    return expirationDate.valueOf() < Date.now();
};
