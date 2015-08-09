#!/usr/bin/ruby
require 'date'
require './bank_account'
require './transaction_record'
require './transaction_collection'


file_name = "../../../../../csv_files/asb_23072015.csv"
csv_type = "ASB"

bank_acc = BankAccount.new

fileObj = File.new(file_name, "r")
line_counter = 1

# Run through CSV file and populate various objects with the relevant data
while (line = fileObj.gets)
  if(csv_type == "ASB")
    case line_counter
    when 2                                                          # Get account num
      bank_acc.account_num = line.scan(/^Bank (\d+); Branch (\d+); Account ([\d\-]+)/).to_a.join("-")
    
    when 3                                                          # Get date that records start from
      bank_acc.transaction_collection.date_from = DateTime.strptime(/From date (\d{8})/.match(line)[1], "%Y%m%d").strftime("%Y/%m/%d")
    
    when 4                                                          # Get date that records go til
      bank_acc.transaction_collection.date_to = DateTime.strptime(/To date (\d{8})/.match(line)[1], "%Y%m%d").strftime("%Y/%m/%d")
    
    when 6                                                          # Get acc balance
      bank_acc.balance = /Ledger Balance : ([\d\.]+)/.match(line)[1]
    
    when 9..(Float::INFINITY)                                       # Get all the transaction details; populate Transaction objects
    bank_acc.transaction_collection.push(TransactionRecord.new(line.scan(/^(.*),(.*),(.*),(.*),"(.*)","(.*)",(.*)$/).to_a.flatten))
    
    end
  end
  line_counter += 1
end

puts "Account: #{bank_acc.account_num}"
puts "From #{bank_acc.transaction_collection.date_from} to #{bank_acc.transaction_collection.date_to}"
puts bank_acc.transaction_collection.to_s


total = 0
bank_acc.transaction_collection.transactions.each do |t|

    total += t.amount
    puts t.date + ' %.2f' % total
end

fileObj.close