class TimeEntryType < ActiveRecord::Base
  has_many :time_entries
  has_many :time_entry_type_materials
  has_many :materials, :through => :time_entry_type_materials
  belongs_to :time_entry_type
  has_one :time_entry_type

  validates :description, :presence => true
  
  scope :active, where(:valid_until => nil)
  
  def self.for_select
    active.collect {|t| [ t, t.id ] }
  end
  
  def to_s
    s = [description]
    s << valid_until unless valid_until.nil?
    s.join(', ')
  end
end
