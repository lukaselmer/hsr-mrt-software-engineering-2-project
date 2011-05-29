# Holds all information about a Customer
class Customer < ActiveRecord::Base
  has_many :time_entries
  has_many :orders
  belongs_to :address

  scope :active, where(:deleted_at => nil)
  scope :updated_after, lambda {|last_update| where("updated_at > :last_update OR created_at > :last_update OR deleted_at > :last_update",
      :last_update => last_update) }

  validates :first_name, :last_name, :presence => true

  accepts_nested_attributes_for :address, :allow_destroy => true

  def self.for_select(customer = nil)
    
    customers = active.order("last_name asc").collect { |c| [c, c.id] }
    customers += [[customer, customer.id]] unless customer.nil? || customer.deleted_at.nil?
      
    return customers
  end

  def to_s
    s = [ last_name, first_name ].join(', ')
    s << " (" << I18n.translate(:until, :default => "bis") << " " << I18n.localize(deleted_at, :format => :short) << ")" unless deleted_at.nil? 
    return s
  end
end
