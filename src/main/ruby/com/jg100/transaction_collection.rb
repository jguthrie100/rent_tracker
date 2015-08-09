class TransactionCollection
  attr_accessor :transactions, :date_from, :date_to
  
  def initialize()
    @transactions = Array.new
  end
  
  def push(t)            # Shortcut method to push records to transactions array
    transactions.push(t)
  end
  
  def to_s
    transactions.each do |t|
      puts t.to_s
    end
  end
end