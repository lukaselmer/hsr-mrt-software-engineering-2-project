class Address < ActiveRecord::Base
  has_one :order
  has_one :customer
end
