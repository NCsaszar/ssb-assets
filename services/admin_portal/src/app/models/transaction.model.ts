export interface Transaction {
  transactionId: number;
  accountId: number;
  accountNumber: string;
  transactionType: string;
  amount: number;
  description: string;
  dateTime: string | null;
  isCredit: boolean;
}
export interface TransactionResponse {
  data: {
    content: Transaction[];
    pageable: {
      pageNumber: number;
      pageSize: number;
    };
    totalPages: number;
    totalElements: number;
  };
}
