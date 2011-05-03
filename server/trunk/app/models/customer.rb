class Customer < ActiveRecord::Base
  has_many :time_entries
  has_many :orders
  belongs_to :address
  belongs_to :location
  
  scope :updated_after, lambda {|last_update| where("updated_at > :last_update OR created_at > :last_update OR deleted_at > :last_update",
    :last_update => last_update) }

  validates :first_name, :last_name, :presence => true

  accepts_nested_attributes_for :address, :allow_destroy => true
end
