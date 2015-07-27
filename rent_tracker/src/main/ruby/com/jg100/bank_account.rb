class BankAccount
    attr_accessor :account_num, :balance, :transaction_collection
    
    def initialize()
        @transaction_collection = TransactionCollection.new
    end
    
    def to_s
        puts "Account Num: #{account_num}; Acc Balance: #{balance}"
    end
end