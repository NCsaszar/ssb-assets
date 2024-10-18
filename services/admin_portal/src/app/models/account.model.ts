export interface Account {
  userId: number;
  accountId: number;
  accountNumber: string;
  accountType: string;
  balance: number;
  createdAt: string | null;
  isActive: boolean;
}
export interface AccountResponse {
  data: {
    content: Account[];
    pageable: {
      pageNumber: number;
      pageSize: number;
    };
    totalPages: number;
    totalElements: number;
  };
}
