# Holds all information about a TimeEntryType
class TimeEntryType < ActiveRecord::Base
  has_many :time_entries
  has_many :time_entry_type_materials
  has_many :materials, :through => :time_entry_type_materials
  belongs_to :time_entry_type
  has_one :time_entry_type

  validates :description, :presence => true, :length => { :within => 1..50 }
  
  scope :active, where(:valid_until => nil)
  scope :updated_after, lambda {|last_update| where("updated_at > :last_update OR created_at > :last_update OR valid_until > :last_update",
    :last_update => last_update) }
  
  def self.for_select
    active.order("description asc").collect {|t| [ t, t.id ] }
  end
  
  def to_s
    s = [description].join(", ")
    s += " (" + I18n.t(:until, :default => "bis") + " "  + I18n.localize(valid_until, :format => :short) + ")" unless valid_until.nil?
    return s
  end
end
